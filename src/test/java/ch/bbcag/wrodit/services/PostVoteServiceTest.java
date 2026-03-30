package ch.bbcag.wrodit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entities.Post;
import ch.bbcag.wrodit.entities.PostsVote;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.repos.PostRepository;
import ch.bbcag.wrodit.repos.PostsVoteRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

class PostVoteServiceTest {
  private PostsVoteRepository postsVoteRepositoryMock;
  private PostRepository postRepositoryMock;
  private UserRepository userRepositoryMock;

  private PostVoteService postVoteService;

  private Post mockPost;
  private User mockUser;
  private PostsVote mockVote;

  @BeforeEach
  void setup() {
    postsVoteRepositoryMock = Mockito.mock(PostsVoteRepository.class);
    postRepositoryMock = Mockito.mock(PostRepository.class);
    userRepositoryMock = Mockito.mock(UserRepository.class);

    postVoteService =
        new PostVoteService(postsVoteRepositoryMock, postRepositoryMock, userRepositoryMock);

    mockPost = TestingUtil.generatePosts(1)[0];
    mockUser = TestingUtil.generateUser();
    mockVote = new PostsVote();
    mockVote.setUsers(mockUser);
    mockVote.setPosts(mockPost);
  }

  // get
  @Test
  void checkGetVote_whenValid_thenSuccess() {
    when(postsVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(mockVote));

    PostsVote result = postVoteService.find(1, 1);

    assertEquals(mockVote.getVote(), result.getVote());
    assertEquals(mockVote.getId(), result.getId());
  }

  @Test
  void checkGetVote_whenInvalidId_then404() {
    when(postsVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> postVoteService.find(1, 1));
  }

  // delete
  @Test
  void checkDeleteById_whenValid_thenSuccess() {
    PostsVote vote = new PostsVote();
    vote.setUsers(mockUser);
    vote.setPosts(mockPost);

    when(postsVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.of(vote));
    doNothing().when(postsVoteRepositoryMock).deleteById(anyInt());

    assertDoesNotThrow(() -> postVoteService.deleteById(1, 1));
  }

  @Test
  void checkDeleteById_whenUnauthorized_thenThrow() {
    when(postsVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(mockVote));
    doNothing().when(postsVoteRepositoryMock).deleteById(anyInt());

    assertDoesNotThrow(() -> postVoteService.deleteById(1, 2));
  }

  @Test
  void checkDeleteById_whenNotFound_thenThrow() {
    when(postsVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.empty());
    doNothing().when(postsVoteRepositoryMock).deleteById(anyInt());

    assertThrows(EntityNotFoundException.class, () -> postVoteService.deleteById(1, 2));
  }

  // update
  @Test
  void checkUpdate_whenExists_thenSuccess() {
    when(postRepositoryMock.existsById(any())).thenReturn(true);
    when(userRepositoryMock.existsById(any())).thenReturn(true);

    when(postsVoteRepositoryMock.findOne(any(Specification.class)))
        .thenReturn(Optional.of(mockVote));
    when(postsVoteRepositoryMock.save(any(PostsVote.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    PostsVote result = postVoteService.update(1, mockPost.getId(), mockUser.getId());

    assertEquals(1, result.getVote());
    verify(postsVoteRepositoryMock).save(any(PostsVote.class));
  }

  @Test
  void checkUpdate_whenNotExists_thenSuccess() {
    when(postRepositoryMock.existsById(any())).thenReturn(true);
    when(userRepositoryMock.existsById(any())).thenReturn(true);

    when(postsVoteRepositoryMock.findOne(any(Specification.class))).thenReturn(Optional.empty());
    when(postsVoteRepositoryMock.save(any(PostsVote.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    PostsVote result = postVoteService.update(1, mockPost.getId(), mockUser.getId());

    assertEquals(1, result.getVote());
    verify(postsVoteRepositoryMock).save(any(PostsVote.class));
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
