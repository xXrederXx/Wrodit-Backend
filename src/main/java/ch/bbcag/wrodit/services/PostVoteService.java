package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entities.PostVote;
import ch.bbcag.wrodit.repos.PostRepository;
import ch.bbcag.wrodit.repos.PostVoteRepository;
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
  private final PostVoteRepository PostVoteRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostVoteService(
      PostVoteRepository PostVoteRepository,
      PostRepository postRepository,
      UserRepository userRepository) {
    this.PostVoteRepository = PostVoteRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public PostVote update(Integer vote, Integer postId, Integer userId) {
    if (!postRepository.existsById(postId) || !userRepository.existsById(userId)) {
      throw new EntityNotFoundException();
    }

    Optional<PostVote> existing = PostVoteRepository.findOne(buildSpecification(userId, postId));

    PostVote entity;
    if (existing.isPresent()) {
      entity = existing.get();
    } else {
      entity = new PostVote();
      entity.setPosts(postRepository.getReferenceById(postId));
      entity.setUsers(userRepository.getReferenceById(userId));
    }
    entity.setVote(vote);

    return PostVoteRepository.save(entity);
  }

  public void deleteById(Integer userId, Integer postId) {
    PostVote entity =
        PostVoteRepository.findOne(buildSpecification(userId, postId))
            .orElseThrow(EntityNotFoundException::new);
    ThrowHelper.throwAccessDeniedIfNotEqual(entity.getUsers().getId(), userId);
    PostVoteRepository.deleteById(entity.getId());
  }

  public PostVote find(Integer postId, Integer userId) {
    return PostVoteRepository.findOne(buildSpecification(userId, postId))
        .orElseThrow(EntityNotFoundException::new);
  }

  private Specification<PostVote> buildSpecification(Integer userId, Integer postId) {
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
