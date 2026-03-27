package ch.bbcag.wrodit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entities.Comment;
import ch.bbcag.wrodit.entities.CommentVote;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.repos.CommentRepository;
import ch.bbcag.wrodit.repos.CommentVoteRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

class CommentVoteServiceTest {
  private CommentVoteRepository commentsVoteRepositoryMock;
  private CommentRepository commentRepositoryMock;
  private UserRepository userRepositoryMock;

  private CommentVoteService commentVoteService;

  private Comment mockComment;
  private User mockUser;
  private CommentVote mockVote;

  @BeforeEach
  void setup() {
    commentsVoteRepositoryMock = Mockito.mock(CommentVoteRepository.class);
    commentRepositoryMock = Mockito.mock(CommentRepository.class);
    userRepositoryMock = Mockito.mock(UserRepository.class);

    commentVoteService =
        new CommentVoteService(
            commentRepositoryMock, userRepositoryMock, commentsVoteRepositoryMock);

    mockComment = TestingUtil.generateComments(1)[0];
    mockUser = TestingUtil.generateUser();
    mockVote = new CommentVote();
    mockVote.setUsers(mockUser);
    mockVote.setComments(mockComment);
  }

  @Test
  void checkDeleteById_whenValid_thenSuccess() {
    CommentVote vote = new CommentVote();
    vote.setUsers(mockUser);
    vote.setComments(mockComment);

    when(commentsVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(vote));
    doNothing().when(commentsVoteRepositoryMock).deleteById(anyInt());

    assertDoesNotThrow(() -> commentVoteService.deleteById(1, 1));
  }

  @Test
  void checkDeleteById_whenUnauthorized_thenThrow() {
    when(commentsVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(mockVote));
    doNothing().when(commentsVoteRepositoryMock).deleteById(anyInt());

    assertDoesNotThrow(() -> commentVoteService.deleteById(1, 2));
  }

  @Test
  void checkDeleteById_whenNotFound_thenThrow() {
    when(commentsVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.empty());
    doNothing().when(commentsVoteRepositoryMock).deleteById(anyInt());

    assertThrows(EntityNotFoundException.class, () -> commentVoteService.deleteById(1, 2));
  }

  @Test
  void checkUpdate_whenExists_thenSuccess() {
    when(commentRepositoryMock.existsById(any())).thenReturn(true);
    when(userRepositoryMock.existsById(any())).thenReturn(true);

    when(commentsVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(mockVote));
    when(commentsVoteRepositoryMock.save(any(CommentVote.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    CommentVote result = commentVoteService.update(1, mockComment.getId(), mockUser.getId());

    assertEquals(1, result.getVote());
    verify(commentsVoteRepositoryMock).save(any(CommentVote.class));
  }

  @Test
  void checkUpdate_whenNotExists_thenSuccess() {
    when(commentRepositoryMock.existsById(any())).thenReturn(true);
    when(userRepositoryMock.existsById(any())).thenReturn(true);

    when(commentsVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.empty());
    when(commentsVoteRepositoryMock.save(any(CommentVote.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    CommentVote result = commentVoteService.update(1, mockComment.getId(), mockUser.getId());

    assertEquals(1, result.getVote());
    verify(commentsVoteRepositoryMock).save(any(CommentVote.class));
  }

  @Test
  void checkUpdate_whenCommentNotExists_thenThrow() {
    when(commentRepositoryMock.existsById(any())).thenReturn(false);
    when(userRepositoryMock.existsById(any())).thenReturn(true);

    assertThrows(
        EntityNotFoundException.class,
        () -> commentVoteService.update(1, mockComment.getId(), mockUser.getId()));
  }

  @Test
  void checkUpdate_whenUserNotExists_thenThrow() {
    when(commentRepositoryMock.existsById(any())).thenReturn(true);
    when(userRepositoryMock.existsById(any())).thenReturn(false);

    assertThrows(
        EntityNotFoundException.class,
        () -> commentVoteService.update(1, mockComment.getId(), mockUser.getId()));
  }
}
