package ch.bbcag.wrodit.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.wrodit.entitys.Comment;
import ch.bbcag.wrodit.services.CommentService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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

  private static Comment mockComment;
  private static Page<Comment> mockCommentPage;

  @BeforeAll
  static void inti() {
    Comment[] comments = new Comment[10];
    for (int i = 0; i < 10; i++) {
      var comment = new Comment();
      comment.setId(1);
      comment.setContent("MOCK CONTENT");
      comment.setCreatedAt(OffsetDateTime.of(2026, 3, 20, 9, 13, 21, 67, ZoneOffset.UTC));
      comments[i] = comment;
    }
    mockCommentPage = new PageImpl<>(Arrays.stream(comments).toList());
    mockComment = comments[0];
  }

  @Test
  @Disabled("Needs to be resolved, issue on gitlab")
  void checkGetComments_whenCommentExists_thenSuccess() throws Exception {
    Mockito.when(
            commentService.getPaginatedComments(
                any(Pageable.class), any(Integer.class), any(Integer.class)))
        .thenReturn(mockCommentPage);

    mockMvc.perform(get(CommentController.PATH + "/")).andExpect(status().isOk());
  }

  @Test
  @Disabled("Needs to be resolved, issue on gitlab")
  void checkGetThreads_whenNoThreadExists_thenSuccess() throws Exception {
    Mockito.when(
            commentService.getPaginatedComments(
                any(Pageable.class), any(Integer.class), any(Integer.class)))
        .thenReturn(new PageImpl<>(List.of()));

    mockMvc.perform(get(ThreadController.PATH + "/")).andExpect(status().isOk());
  }
}
