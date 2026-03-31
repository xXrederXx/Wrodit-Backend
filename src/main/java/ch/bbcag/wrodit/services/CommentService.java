package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entities.Comment;
import ch.bbcag.wrodit.repos.CommentRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import ch.bbcag.wrodit.util.ThrowHelper;
import ch.bbcag.wrodit.util.exception.FailedValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
  }

  public Comment getCommentById(Integer id) {
    return commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public Page<Comment> getPaginatedComments(Pageable pageable, Integer postId, Integer parentId) {
    return commentRepository.findAll(buildSpecification(postId, parentId), pageable);
  }

  private Specification<Comment> buildSpecification(Integer postId, Integer parentId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (postId != null) {
        predicates.add(criteriaBuilder.equal(root.get("posts").get("id"), postId));
      }
      if (parentId != null) {
        predicates.add(criteriaBuilder.equal(root.get("parentComment").get("id"), parentId));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public Comment save(Comment comment, Integer userId) {
    checkCommentForeignKeys(comment);
    if (!userRepository.existsById(userId)) {
      throw new EntityNotFoundException();
    }
    comment.setCreatedAt(OffsetDateTime.now());
    comment.setUsers(userRepository.getReferenceById(userId));
    return commentRepository.save(comment);
  }

  public Comment update(Comment comment, Integer commentId, Integer authId) {
    checkCommentForeignKeys(comment);
    Comment existing = this.getCommentById(commentId);

    ThrowHelper.throwAccessDeniedIfNotEqual(existing.getUsers().getId(), authId);

    mergeComment(existing, comment);
    return commentRepository.save(existing);
  }

  private void mergeComment(Comment existing, Comment changing) {
    Map<String, List<String>> errors = new HashMap<>();

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

  private void checkCommentForeignKeys(Comment comment)
  {
    if((comment.getPosts() == null && comment.getParentComment() == null) || (comment.getPosts() != null && comment.getParentComment() != null))
    {
      Map<String, List<String>> errors = new HashMap<>();
      errors.put("references", List.of("Only one of the Ids (parentId, postId) must be set"));
      throw new FailedValidationException(errors);
    }
  }

  public void deletePostById(Integer id, Integer authId) {
    Comment comment = this.getCommentById(id);
    ThrowHelper.throwAccessDeniedIfNotEqual(comment.getUsers().getId(), authId);
    commentRepository.deleteById(id);
  }
}
