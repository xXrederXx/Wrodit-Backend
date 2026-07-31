package ch.bbcag.wrodit.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ch.bbcag.wrodit.entities.CommentVote;
import ch.bbcag.wrodit.repos.CommentRepository;
import ch.bbcag.wrodit.repos.CommentVoteRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import ch.bbcag.wrodit.util.ThrowHelper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

@Service
public class CommentVoteService {
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final CommentVoteRepository commentVoteRepository;

  public CommentVoteService(
      CommentRepository commentRepository,
      UserRepository userRepository,
      CommentVoteRepository commentVoteRepository) {
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
    this.commentVoteRepository = commentVoteRepository;
  }

  public CommentVote update(Integer vote, Integer commentId, Integer userId) {
    Optional<CommentVote> existing = commentVoteRepository.findOne(buildSpecification(userId, commentId));

    CommentVote entity;
    if (existing.isPresent()) {
      entity = existing.get();
    } else {
      entity = createVoteEntity(userId, commentId);
    }
    entity.setVote(vote);

    return commentVoteRepository.save(entity);
  }

  private Specification<CommentVote> buildSpecification(Integer userId, Integer commentId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (userId != null) {
        predicates.add(criteriaBuilder.equal(root.get("users").get("id"), userId));
      }
      if (commentId != null) {
        predicates.add(criteriaBuilder.equal(root.get("comments").get("id"), commentId));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public void deleteById(Integer userId, Integer commentId) {
    CommentVote entity = commentVoteRepository
        .findOne(buildSpecification(userId, commentId))
        .orElseThrow(EntityNotFoundException::new);
    ThrowHelper.throwAccessDeniedIfNotEqual(entity.getUsers().getId(), userId);
    commentVoteRepository.deleteById(entity.getId());
  }

  public CommentVote find(Integer commentId, Integer userId) {
    return commentVoteRepository
        .findOne(buildSpecification(userId, commentId))
        .orElseGet(()->createVoteEntity(userId, commentId));
  }

  private CommentVote createVoteEntity(Integer userId, Integer commentId) {
    if (!commentRepository.existsById(commentId) || !userRepository.existsById(userId)) {
      throw new EntityNotFoundException();
    }

    CommentVote entity = new CommentVote();
    entity.setUsers(userRepository.getReferenceById(userId));
    entity.setComments(commentRepository.getReferenceById(commentId));
    return entity;
  }
}
