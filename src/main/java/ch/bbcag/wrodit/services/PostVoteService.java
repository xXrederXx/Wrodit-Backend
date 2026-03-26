package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entities.PostsVote;
import ch.bbcag.wrodit.repos.PostRepository;
import ch.bbcag.wrodit.repos.PostsVoteRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import ch.bbcag.wrodit.util.ThrowHelper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PostVoteService {
  private final PostsVoteRepository postsVoteRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostVoteService(
      PostsVoteRepository postsVoteRepository,
      PostRepository postRepository,
      UserRepository userRepository) {
    this.postsVoteRepository = postsVoteRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public PostsVote update(Integer vote, Integer postId, Integer userId) {
    if (!postRepository.existsById(postId) || !userRepository.existsById(userId)) {
      throw new EntityNotFoundException();
    }

    Optional<PostsVote> existing = postsVoteRepository.findOne(buildSpecification(userId, postId));

    PostsVote entity;
    if (existing.isPresent()) {
      entity = existing.get();
    } else {
      entity = new PostsVote();
      entity.setPosts(postRepository.getReferenceById(postId));
      entity.setUsers(userRepository.getReferenceById(userId));
    }
    entity.setVote(vote);

    return postsVoteRepository.save(entity);
  }

  public void deleteById(Integer userId, Integer postId) {
    PostsVote entity =
        postsVoteRepository
            .findOne(buildSpecification(userId, postId))
            .orElseThrow(EntityNotFoundException::new);
    ThrowHelper.throwAuthorizationIfNotEqual(entity.getUsers().getId(), userId);
    postsVoteRepository.deleteById(postId);
  }

  private Specification<PostsVote> buildSpecification(Integer userId, Integer postId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (userId != null) {
        predicates.add(criteriaBuilder.equal(root.get("users").get("id"), userId));
      }
      if (postId != null) {
        predicates.add(criteriaBuilder.equal(root.get("posts").get("id"), postId));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
