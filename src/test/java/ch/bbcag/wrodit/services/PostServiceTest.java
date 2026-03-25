package ch.bbcag.wrodit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entitys.Post;
import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.repos.PostRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import ch.bbcag.wrodit.util.exception.FailedValidationException;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authorization.AuthorizationDeniedException;

class PostServiceTest {

  private PostRepository mockPostRepo;
  private UserRepository mockUserRepo;
  private PostService postService;

  private Post mockPost;
  private Page<Post> mockPostPage;
  private User mockUser;

  @BeforeEach
  void setup() {
    mockPostRepo = Mockito.mock(PostRepository.class);
    mockUserRepo = Mockito.mock(UserRepository.class);
    postService = new PostService(mockPostRepo, mockUserRepo);

    mockPost = TestingUtil.generatePosts(1)[0];
    mockUser = TestingUtil.generateUser();

    mockPostPage = new PageImpl<>(Arrays.stream(TestingUtil.generatePosts(10)).toList());
  }

  @Test
  void checkFindById_whenValidId_thenReturn() {
    when(mockPostRepo.findById(anyInt())).thenReturn(Optional.of(mockPost));

    assertEquals(mockPost, postService.getPostById(1));
  }

  @Test
  void checkFindById_whenInvalidId_thenThrow() {
    when(mockPostRepo.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> postService.getPostById(1));
  }

  @Test
  void checkFindByUsername_whenValidUsername_thenReturn() {
    when(mockPostRepo.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(mockPostPage);

    assertEquals(mockPostPage, postService.getPaginatedPosts(1, 1, PageRequest.of(0, 10)));
  }

  @Test
  void checkDeleteById_whenValid_thenSuccess() {
    Post post = new Post(1);
    post.setUsers(new User(1));

    when(mockPostRepo.findById(anyInt())).thenReturn(Optional.of(post));
    doNothing().when(mockPostRepo).deleteById(anyInt());

    assertDoesNotThrow(() -> postService.deletePostById(1, 1));
  }

  @Test
  void checkDeleteById_whenUnauthorized_thenThrow() {
    Post post = new Post(1);
    post.setUsers(new User(1));

    when(mockPostRepo.findById(anyInt())).thenReturn(Optional.of(post));
    doNothing().when(mockPostRepo).deleteById(anyInt());

    assertThrows(AuthorizationDeniedException.class, () -> postService.deletePostById(1, 2));
  }

  @Test
  void checkSave_whenValidPost_thenSuccess() {
    Post post = TestingUtil.generatePosts(1)[0];

    when(mockUserRepo.getReferenceById(anyInt())).thenReturn(mockUser);
    when(mockPostRepo.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Post result = postService.save(post, mockUser.getId());

    assertEquals(mockUser.getId(), result.getUsers().getId());
    Assertions.assertTrue(
        OffsetDateTime.now().toEpochSecond() - result.getCreatedAt().toEpochSecond()
            < TestingUtil.MAX_TIME_CHECK_DIFF);
    verify(mockUserRepo).getReferenceById(anyInt());
    verify(mockPostRepo).save(post);
  }

  @Test
  void checkUpdate_whenAllValidChanges_thenSuccess() {
    User user = new User(1);
    Post post = TestingUtil.generatePosts(1)[0];
    post.setUsers(user);

    Post change = new Post();
    change.setContent("change");
    change.setTitle("change");

    when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));
    when(mockPostRepo.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Post result = postService.update(change, post.getId(), user.getId());

    assertEquals(change.getTitle(), result.getTitle());
    assertEquals(change.getContent(), result.getContent());
  }

  @Test
  void checkUpdate_whenNoChanges_thenSuccess() {
    User user = new User(1);
    Post post = TestingUtil.generatePosts(1)[0];
    post.setUsers(user);

    Post change = new Post();
    change.setContent(null);
    change.setTitle(null);

    when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));
    when(mockPostRepo.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Post result = postService.update(change, post.getId(), user.getId());

    assertEquals(post.getTitle(), result.getTitle());
    assertEquals(post.getContent(), result.getContent());
  }

  @Test
  void checkUpdate_whenInvalidChanges_thenThrows() {
    User user = new User(1);
    Post post = TestingUtil.generatePosts(1)[0];
    post.setUsers(user);

    Post change = new Post();
    change.setContent(null);
    change.setTitle("Title too long".repeat(20));

    when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));
    when(mockPostRepo.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

    assertThrows(
        FailedValidationException.class,
        () -> postService.update(change, post.getId(), user.getId()));
  }

  @Test
  void checkUpdate_whenNoAuth_thenThrows() {
    User user = new User(1);
    Post post = TestingUtil.generatePosts(1)[0];
    post.setUsers(user);

    Post change = new Post();
    change.setContent(null);
    change.setTitle("Title too long".repeat(20));

    when(mockPostRepo.findById(post.getId())).thenReturn(Optional.of(post));
    when(mockPostRepo.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

    assertThrows(
        AuthorizationDeniedException.class,
        () -> postService.update(change, post.getId(), user.getId() + 1));
  }
}
