package ch.bbcag.wrodit.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entities.Comment;
import ch.bbcag.wrodit.services.CommentService;
import ch.bbcag.wrodit.services.CommentVoteService;
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

@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private CommentService commentService;
  @MockitoBean private CommentVoteService commentVoteService;

  private static Comment mockComment;
  private static Page<Comment> mockCommentPage;

  @BeforeAll
  static void init() {
    Comment[] comments = TestingUtil.generateComments(10);
    mockCommentPage = new PageImpl<>(Arrays.stream(comments).toList());
    mockComment = comments[0];
  }

  @Test
  void checkGetById_whenValidComment_thenIsReturned() throws Exception {
    Mockito.when(commentService.getCommentById(any(Integer.class))).thenReturn(mockComment);

    mockMvc
        .perform(get(URIHelper.join(CommentController.PATH, "1")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(mockComment.getId())))
        .andExpect(jsonPath("$.content", is(mockComment.getContent())));
  }

  @Test
  void checkGetById_whenNoComment_then404Returned() throws Exception {
    Mockito.when(commentService.getCommentById(any(Integer.class)))
        .thenThrow(EntityNotFoundException.class);

    mockMvc
        .perform(get(URIHelper.join(CommentController.PATH, "1")))
        .andExpect(status().isNotFound());
  }

  @Test
  void checkGetComments_whenCommentExists_thenSuccess() throws Exception {
    Mockito.when(commentService.getPaginatedComments(any(Pageable.class), any(), any()))
        .thenReturn(mockCommentPage);

    mockMvc.perform(get(URIHelper.join(CommentController.PATH, ""))).andExpect(status().isOk());
  }

  @Test
  void checkGetComments_whenNoCommentsExists_thenSuccess() throws Exception {
    Mockito.when(commentService.getPaginatedComments(any(Pageable.class), any(), any()))
        .thenReturn(new PageImpl<>(List.of()));

    mockMvc.perform(get(URIHelper.join(CommentController.PATH, ""))).andExpect(status().isOk());
  }

  @Test
  void checkPostComments_whenValidComment_thenCreated() throws Exception {
    Mockito.when(commentService.save(any(Comment.class), any())).thenReturn(mockComment);

    mockMvc
        .perform(
            post(URIHelper.join(CommentController.PATH, ""))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    String.format(
                        """
                                            {
                                                "content":"%s",
                                                "postId":%s
                                            }
                                            """,
                        mockComment.getContent(), mockComment.getPosts().getId())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content", is(mockComment.getContent())));
  }

  @Test
  void checkPostComments_whenInvalidComment_thenBadRequest() throws Exception {
    mockMvc
        .perform(
            post(URIHelper.join(CommentController.PATH, ""))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                                        {
                                            "not_name":"HELP"
                                        }
                                        """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void checkPatchComment_whenValidComment_thenOk() throws Exception {
    Mockito.when(commentService.update(any(Comment.class), any(), any())).thenReturn(mockComment);

    mockMvc
        .perform(
            patch(URIHelper.join(CommentController.PATH, "1"))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                                                {
                                                  "content":"new content"
                                                }
                                        """))
        .andExpect(status().isOk());
  }

  @Test
  void checkDeleteComment_whenValidId_thenNoContent() throws Exception {
    Mockito.doNothing().when(commentService).deletePostById(any(), any());
    mockMvc
        .perform(delete(URIHelper.join(CommentController.PATH, "1")))
        .andExpect(status().isNoContent());
  }

  @Test
  void checkDeleteComment_whenInvalidId_thenNotFound() throws Exception {
    Mockito.doThrow(EntityNotFoundException.class)
        .when(commentService)
        .deletePostById(any(), any());
    mockMvc
        .perform(delete(URIHelper.join(CommentController.PATH, "1")))
        .andExpect(status().isNotFound());
  }

  @Test
  void checkVote_whenValidId_thenOk() throws Exception {
    Mockito.when(commentVoteService.update(anyInt(), anyInt(), anyInt()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    mockMvc
        .perform(
            put(URIHelper.join(CommentController.PATH, "1/vote"))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                            {
                              "vote":1
                            }
                            """))
        .andExpect(status().isNoContent());
  }

  @Test
  void checkVote_whenInvalidId_thenNotFound() throws Exception {
    Mockito.doThrow(EntityNotFoundException.class)
        .when(commentVoteService)
        .update(any(), any(), any());

    mockMvc
        .perform(
            put(URIHelper.join(CommentController.PATH, "1/vote"))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                            {
                              "vote":1
                            }
                            """))
        .andExpect(status().isNotFound());
  }

  @Test
  void checkVote_whenInvalidVote_thenNotFound() throws Exception {
    mockMvc
        .perform(
            put(URIHelper.join(CommentController.PATH, "1/vote"))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                            {
                              "vote":2
                            }
                            """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void checkDeleteVote_whenValidVote_thenOk() throws Exception {
    Mockito.doNothing().when(commentVoteService).deleteById(any(), any());

    mockMvc
        .perform(delete(URIHelper.join(CommentController.PATH, "1/vote")))
        .andExpect(status().isNoContent());
  }

  @Test
  void checkDeleteVote_whenVoteNotFound_theNotFound() throws Exception {
    Mockito.doThrow(EntityNotFoundException.class)
        .when(commentVoteService)
        .deleteById(any(), any());
    ;

    mockMvc
        .perform(delete(URIHelper.join(CommentController.PATH, "1/vote")))
        .andExpect(status().isNotFound());
  }
}
