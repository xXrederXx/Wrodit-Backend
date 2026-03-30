package ch.bbcag.wrodit.mappers;

import static org.junit.jupiter.api.Assertions.*;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.dto.request.CommentCreateDTO;
import ch.bbcag.wrodit.dto.request.CommentRequestDTO;
import ch.bbcag.wrodit.dto.response.CommentPageResponseDTO;
import ch.bbcag.wrodit.dto.response.CommentResponseDTO;
import ch.bbcag.wrodit.entities.Comment;
import ch.bbcag.wrodit.entities.CommentVote;
import ch.bbcag.wrodit.mapper.CommentMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

class CommentMapperTest {
  @Test
  void checkToDto_whenValidEntity_thenDto() {
    Comment mockComment = TestingUtil.generateComments(1)[0];
    CommentResponseDTO dto = CommentMapper.toDto(mockComment);

    assertEquals(mockComment.getId(), dto.id());
    assertEquals(mockComment.getPosts().getId(), dto.postId());
    assertEquals(mockComment.getParentComment().getId(), dto.parentId());
    assertEquals(mockComment.getUsers().getId(), dto.userId());
    assertEquals(mockComment.getContent(), dto.content());
    assertEquals(
        mockComment.getCommentVotes().stream().mapToInt(CommentVote::getVote).sum(), dto.votes());
    assertEquals(mockComment.getCreatedAt(), dto.createdAt());
  }

  @Test
  void checkToDto_whenValidPage_thenDto() {
    Page<Comment> mockCommentPage =
        new PageImpl<>(Arrays.stream(TestingUtil.generateComments(10)).toList());
    CommentPageResponseDTO dto = CommentMapper.toDto(mockCommentPage);

    assertEquals(10, dto.getContent().size());
    assertNotNull(dto.getPage());
    assertNotNull(dto.getFirst());
    assertNotNull(dto.getLast());
    assertNotNull(dto.getSize());
    assertNotNull(dto.getTotalPages());
    assertNotNull(dto.getTotalElements());
  }

  @Test
  void checkFromDto_whenValidEntity_thenDto() {
    CommentRequestDTO dto = generateDto();
    Comment comment = CommentMapper.fromDto(dto);

    assertEquals(dto.content(), comment.getContent());
  }

  @Test
  void checkFromCreateDto_whenValidEntity_thenDto() {
    CommentCreateDTO dto = generateCreateDto();
    Comment comment = CommentMapper.fromDto(dto);

    assertEquals(dto.content(), comment.getContent());
    assertEquals(dto.parentId(), comment.getParentComment().getId());
    assertEquals(dto.postId(), comment.getPosts().getId());
  }

  private CommentRequestDTO generateDto() {
    return new CommentRequestDTO("Tester");
  }

  private CommentCreateDTO generateCreateDto() {
    return new CommentCreateDTO("Tester", 1, 1);
  }
}
