package ch.bbcag.wrodit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entities.Post;
import ch.bbcag.wrodit.entities.PostVote;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.repos.PostRepository;
import ch.bbcag.wrodit.repos.PostVoteRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

class PostVoteServiceTest {
  private PostVoteRepository PostVoteRepositoryMock;
  private PostRepository postRepositoryMock;
  private UserRepository userRepositoryMock;

  private PostVoteService postVoteService;

  private Post mockPost;
  private User mockUser;
  private PostVote mockVote;

  @BeforeEach
  void setup() {
    PostVoteRepositoryMock = Mockito.mock(PostVoteRepository.class);
    postRepositoryMock = Mockito.mock(PostRepository.class);
    userRepositoryMock = Mockito.mock(UserRepository.class);

    postVoteService =
        new PostVoteService(PostVoteRepositoryMock, postRepositoryMock, userRepositoryMock);

    mockPost = TestingUtil.generatePosts(1)[0];
    mockUser = TestingUtil.generateUser();
    mockVote = new PostVote();
    mockVote.setUsers(mockUser);
    mockVote.setPosts(mockPost);
  }

  // get
  @Test
  void checkGetVote_whenValid_thenSuccess() {
    when(PostVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(mockVote));

    PostVote result = postVoteService.find(1, 1);

    assertEquals(mockVote.getVote(), result.getVote());
    assertEquals(mockVote.getId(), result.getId());
  }

  @Test
  void checkGetVote_whenInvalidId_then404() {
    when(PostVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> postVoteService.find(1, 1));
  }

  // delete
  @Test
  void checkDeleteById_whenValid_thenSuccess() {
    PostVote vote = new PostVote();
    vote.setUsers(mockUser);
    vote.setPosts(mockPost);

    when(PostVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.of(vote));
    doNothing().when(PostVoteRepositoryMock).deleteById(anyInt());

    assertDoesNotThrow(() -> postVoteService.deleteById(1, 1));
  }

  @Test
  void checkDeleteById_whenUnauthorized_thenThrow() {
    when(PostVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(mockVote));
    doNothing().when(PostVoteRepositoryMock).deleteById(anyInt());

    assertDoesNotThrow(() -> postVoteService.deleteById(1, 2));
  }

  @Test
  void checkDeleteById_whenNotFound_thenThrow() {
    when(PostVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.empty());
    doNothing().when(PostVoteRepositoryMock).deleteById(anyInt());

    assertThrows(EntityNotFoundException.class, () -> postVoteService.deleteById(1, 2));
  }

  // update
  @Test
  void checkUpdate_whenExists_thenSuccess() {
    when(postRepositoryMock.existsById(any())).thenReturn(true);
    when(userRepositoryMock.existsById(any())).thenReturn(true);

    when(PostVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(mockVote));
    when(PostVoteRepositoryMock.save(any(PostVote.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    PostVote result = postVoteService.update(1, mockPost.getId(), mockUser.getId());

    assertEquals(1, result.getVote());
    verify(PostVoteRepositoryMock).save(any(PostVote.class));
  }

  @Test
  void checkUpdate_whenNotExists_thenSuccess() {
    when(postRepositoryMock.existsById(any())).thenReturn(true);
    when(userRepositoryMock.existsById(any())).thenReturn(true);

    when(PostVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.empty());
    when(PostVoteRepositoryMock.save(any(PostVote.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    PostVote result = postVoteService.update(1, mockPost.getId(), mockUser.getId());

    assertEquals(1, result.getVote());
    verify(PostVoteRepositoryMock).save(any(PostVote.class));
  }

  @Test
  void checkUpdate_whenPostNotExists_thenThrow() {
    when(postRepositoryMock.existsById(any())).thenReturn(false);
    when(userRepositoryMock.existsById(any())).thenReturn(true);

    assertThrows(
        EntityNotFoundException.class,
        () -> postVoteService.update(1, mockPost.getId(), mockUser.getId()));
  }

  @Test
  void checkUpdate_whenUserNotExists_thenThrow() {
    when(postRepositoryMock.existsById(any())).thenReturn(true);
    when(userRepositoryMock.existsById(any())).thenReturn(false);

    assertThrows(
        EntityNotFoundException.class,
        () -> postVoteService.update(1, mockPost.getId(), mockUser.getId()));
  }
}
