package ch.bbcag.wrodit.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entitys.Post;
import ch.bbcag.wrodit.services.PostService;
import ch.bbcag.wrodit.util.URIHelper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private PostService postService;

  private static Post mockPost;
  private static Page<Post> mockPostPage;

  @BeforeAll
  static void init() {
    Post[] posts = TestingUtil.generatePosts(10);
    mockPostPage = new PageImpl<>(Arrays.stream(posts).toList());
    mockPost = posts[0];
  }

  @Test
  void checkGetById_whenValidPost_thenIsReturned() throws Exception {
    Mockito.when(postService.getPostById(any(Integer.class))).thenReturn(mockPost);

    mockMvc
        .perform(get(URIHelper.join(PostController.PATH, "1")))
        .andExpect(status().isOk())
        // .andExpect(jsonPath("$.id", is(mockPost.getId()))) Uncomment once implemented
        .andExpect(jsonPath("$.title", is(mockPost.getTitle())))
        .andExpect(jsonPath("$.content", is(mockPost.getContent())))
        .andExpect(jsonPath("$.vote", is(0)));
  }

  @Test
  void checkGetById_whenNoPost_then404Returned() throws Exception {
    Mockito.when(postService.getPostById(any(Integer.class)))
        .thenThrow(EntityNotFoundException.class);

    mockMvc.perform(get(URIHelper.join(PostController.PATH, "1"))).andExpect(status().isNotFound());
  }

  @Test
  void checkGetPosts_whenPostExists_thenSuccess() throws Exception {
    Mockito.when(postService.getPaginatedPosts(any(), any(), any(Pageable.class)))
        .thenReturn(mockPostPage);

    mockMvc.perform(get(URIHelper.join(PostController.PATH, ""))).andExpect(status().isOk());
  }

  @Test
  void checkGetPosts_whenNoPostExists_thenSuccess() throws Exception {
    Mockito.when(postService.getPaginatedPosts(any(), any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of()));

    mockMvc.perform(get(URIHelper.join(PostController.PATH, ""))).andExpect(status().isOk());
  }

  @Test
  void checkPostPosts_whenValidPost_thenCreated() throws Exception {
    Mockito.when(postService.save(any(Post.class), any())).thenReturn(mockPost);

    mockMvc
        .perform(
            post(URIHelper.join(PostController.PATH, ""))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    String.format(
                        """
                        {
                            "title":"%s",
                            "content":"%s",
                            "threadId":%s
                        }
                        """,
                        mockPost.getTitle(), mockPost.getContent(), mockPost.getThreads().getId())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title", is(mockPost.getTitle())))
        .andExpect(jsonPath("$.content", is(mockPost.getContent())))
        .andExpect(jsonPath("$.threadId", is(mockPost.getThreads().getId())));
  }

  @Test
  void checkPostPosts_whenInvalidPost_thenBadRequest() throws Exception {
    mockMvc
        .perform(
            post(URIHelper.join(PostController.PATH, ""))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                        {
                            "not_name":"%s",
                            "description":"%s"
                        }
                        """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void checkPatchPost_whenValidPost_thenOk() throws Exception {
    Mockito.when(postService.update(any(Post.class), any(), any())).thenReturn(mockPost);

    mockMvc
        .perform(
            patch(URIHelper.join(PostController.PATH, "1"))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    String.format(
                        """
                            {
                              "title":"new title"
                            }
                    """)))
        .andExpect(status().isOk());
  }

  @Test
  void checkDeletePost_whenValidId_thenNoContent() throws Exception {
    Mockito.doNothing().when(postService).deletePostById(any(), any());
    mockMvc
        .perform(delete(URIHelper.join(PostController.PATH, "1")))
        .andExpect(status().isNoContent());
  }

  @Test
  void checkDeletePost_whenInvalidId_thenNotFound() throws Exception {
    Mockito.doThrow(EntityNotFoundException.class).when(postService).deletePostById(any(), any());
    mockMvc
        .perform(delete(URIHelper.join(PostController.PATH, "1")))
        .andExpect(status().isNotFound());
  }
}
