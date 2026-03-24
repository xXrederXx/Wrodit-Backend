package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entitys.Post;
import ch.bbcag.wrodit.repos.PostRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import ch.bbcag.wrodit.util.ThrowHelper;
import ch.bbcag.wrodit.util.exception.FailedValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PostService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostService(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public Post getPostById(Integer id) {
    return postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public Page<Post> getPaginatedPosts(Integer userId, Integer threadId, Pageable pagable) {
    return postRepository.findAll(buildSpecification(userId, threadId), pagable);
  }

  public void deletePostById(Integer id, Integer userId) {
    Post post = this.getPostById(id);
    ThrowHelper.throwAuthorizationIfNotEqual(post.getUsers().getId(), userId);
    postRepository.deleteById(id);
  }

  public Post save(Post post, Integer authId) {
    post.setUsers(userRepository.getReferenceById(authId));
    post.setCreatedAt(OffsetDateTime.now());
    return postRepository.save(post);
  }

  public Post update(Post post, Integer id, Integer authId) {
    Post existing = this.getPostById(id);
    ThrowHelper.throwAuthorizationIfNotEqual(post.getUsers().getId(), authId);
    mergePost(existing, post);
    return postRepository.save(existing);
  }

  private Specification<Post> buildSpecification(Integer userId, Integer threadId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (userId != null) {
        predicates.add(criteriaBuilder.equal(root.get("users").get("id"), userId));
      }

      if (threadId != null) {
        predicates.add(criteriaBuilder.equal(root.get("threads").get("id"), threadId));
      }

      return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    };
  }

  private void mergePost(Post existing, Post changing) {
    Map<String, List<String>> errors = new HashMap<>();

    if (changing.getTitle() != null) {
      if (StringUtils.isNotBlank(changing.getTitle())) {
        if (changing.getTitle().length() <= 255) {
          existing.setTitle(changing.getTitle());
        } else {
          errors.put("title", List.of("Title cant be longer than 255"));
        }
      } else {
        errors.put("title", List.of("Title cant be empty"));
      }
    }

    if (changing.getContent() != null) {
      if (StringUtils.isNotBlank(changing.getContent())) {
        existing.setContent(changing.getContent());
      } else {
        errors.put("content", List.of("Content cant be empty"));
      }
    }

    if (!errors.isEmpty()) {
      throw new FailedValidationException(errors);
    }
  }
}
