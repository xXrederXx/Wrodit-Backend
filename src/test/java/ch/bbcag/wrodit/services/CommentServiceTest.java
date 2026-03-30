package ch.bbcag.wrodit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entities.Comment;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.repos.CommentRepository;
import ch.bbcag.wrodit.util.exception.FailedValidationException;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;

class CommentServiceTest {
  private CommentRepository mockRepo;
  private CommentService commentService;

  private Comment mockComment;
  private User mockUser;
  private Page<Comment> mockCommentPage;

  @BeforeEach
  void setup() {
    mockRepo = Mockito.mock(CommentRepository.class);
    commentService = new CommentService(mockRepo);

    mockUser = TestingUtil.generateUser();
    mockComment = TestingUtil.generateComments(1)[0];
    mockCommentPage = new PageImpl<>(Arrays.stream(TestingUtil.generateComments(10)).toList());
  }

  // Find by Id
  @Test
  void checkFindById_whenValidId_thenReturn() {
    when(mockRepo.findById(anyInt())).thenReturn(Optional.of(mockComment));

    assertEquals(mockComment, commentService.getCommentById(1));
  }

  @Test
  void checkFindById_whenInvalidId_thenThrow() {
    when(mockRepo.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(1));
  }

  // Find Paginated
  @Test
  void checkFindPaginated_whenValid_thenSuccess() {
    when(mockRepo.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(mockCommentPage);

    assertEquals(mockCommentPage, commentService.getPaginatedComments(PageRequest.of(0, 10), 1, 1));
  }

  // Save
  @Test
  void checkSave_whenValid_thenSuccess() {
    when(mockRepo.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Comment result = commentService.save(mockComment, mockUser.getId());

    assertEquals(mockComment.getId(), result.getId());
    assertEquals(mockComment.getContent(), result.getContent());
    assertEquals(mockComment.getUsers().getId(), mockUser.getId());
    assertTrue(
        OffsetDateTime.now().toEpochSecond() - mockComment.getCreatedAt().toEpochSecond()
            < TestingUtil.MAX_TIME_CHECK_DIFF);
  }

  // Delete By Id
  @Test
  void checkDeleteById_whenValid_thenSuccess() {
    Comment comment = new Comment(1);
    comment.setUsers(new User(1));

    when(mockRepo.findById(anyInt())).thenReturn(Optional.of(comment));
    doNothing().when(mockRepo).deleteById(anyInt());

    assertDoesNotThrow(() -> commentService.deletePostById(1, 1));
  }

  @Test
  void checkDeleteById_whenIdNotFound_then404() {
    when(mockRepo.findById(anyInt())).thenReturn(Optional.empty());
    doNothing().when(mockRepo).deleteById(anyInt());

    assertThrows(EntityNotFoundException.class, () -> commentService.deletePostById(1, 1));
  }

  @Test
  void checkDeleteById_whenUnauthorized_thenThrow() {
    Comment comment = new Comment(1);
    comment.setUsers(new User(1));

    when(mockRepo.findById(anyInt())).thenReturn(Optional.of(comment));
    doNothing().when(mockRepo).deleteById(anyInt());

    assertThrows(AccessDeniedException.class, () -> commentService.deletePostById(1, 2));
  }

  // Update
  @Test
  void checkUpdate_whenAllValidChanges_thenSuccess() {
    User user = new User(1);
    Comment comment = TestingUtil.generateComments(1)[0];
    comment.setUsers(user);

    Comment change = new Comment();
    change.setContent("change");

    when(mockRepo.findById(comment.getId())).thenReturn(Optional.of(comment));
    when(mockRepo.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Comment result = commentService.update(change, comment.getId(), user.getId());

    assertEquals(change.getContent(), result.getContent());
  }

  @Test
  void checkUpdate_whenInvalidId_thenSuccess() {
    User user = new User(1);
    Comment comment = TestingUtil.generateComments(1)[0];
    comment.setUsers(user);

    Comment change = new Comment();
    change.setContent("change");

    when(mockRepo.findById(comment.getId())).thenReturn(Optional.empty());
    when(mockRepo.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    assertThrows(
        EntityNotFoundException.class,
        () -> commentService.update(change, comment.getId(), user.getId()));
  }

  @Test
  void checkUpdate_whenNoChanges_thenSuccess() {
    User user = new User(1);
    Comment comment = TestingUtil.generateComments(1)[0];
    comment.setUsers(user);

    Comment change = new Comment();
    change.setContent(null);

    when(mockRepo.findById(comment.getId())).thenReturn(Optional.of(comment));
    when(mockRepo.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Comment result = commentService.update(change, comment.getId(), user.getId());

    assertEquals(comment.getContent(), result.getContent());
  }

  @Test
  void checkUpdate_whenInvalidChanges_thenThrows() {
    User user = new User(1);
    Comment comment = TestingUtil.generateComments(1)[0];
    comment.setUsers(user);

    Comment change = new Comment();
    change.setContent("");

    when(mockRepo.findById(comment.getId())).thenReturn(Optional.of(comment));
    when(mockRepo.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    assertThrows(
        FailedValidationException.class,
        () -> commentService.update(change, comment.getId(), user.getId()));
  }

  @Test
  void checkUpdate_whenNoAuth_thenThrows() {
    User user = new User(1);
    Comment comment = TestingUtil.generateComments(1)[0];
    comment.setUsers(user);

    Comment change = new Comment();
    change.setContent("");

    when(mockRepo.findById(comment.getId())).thenReturn(Optional.of(comment));
    when(mockRepo.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    assertThrows(
        AccessDeniedException.class,
        () -> commentService.update(change, comment.getId(), user.getId() + 1));
  }
}
