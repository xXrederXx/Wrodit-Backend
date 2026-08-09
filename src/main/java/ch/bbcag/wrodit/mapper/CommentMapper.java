package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.request.CommentCreateDTO;
import ch.bbcag.wrodit.dto.request.CommentRequestDTO;
import ch.bbcag.wrodit.dto.response.CommentPageResponseDTO;
import ch.bbcag.wrodit.dto.response.CommentResponseDTO;
import ch.bbcag.wrodit.entities.Comment;
import ch.bbcag.wrodit.entities.CommentVote;
import ch.bbcag.wrodit.entities.Post;
import org.springframework.data.domain.Page;

public class CommentMapper {
  private CommentMapper() {
    // hide ctor
  }

  public static Comment fromDto(CommentCreateDTO dto) {
    Comment comment = new Comment();
    comment.setContent(dto.content());
    comment.setParentComment(dto.parentId() == null ? null : new Comment(dto.parentId()));
    comment.setPosts(dto.postId() == null ? null : new Post(dto.postId()));
    return comment;
  }

  public static Comment fromDto(CommentRequestDTO dto) {
    Comment comment = new Comment();
    comment.setContent(dto.content());
    return comment;
  }

  public static CommentResponseDTO toDto(Comment comment) {
    return new CommentResponseDTO(
        comment.getId(),
        comment.getContent(),
        comment.getCommentVotes().stream().mapToInt(CommentVote::getVote).sum(),
        comment.getCreatedAt(),
        comment.getParentComment() == null ? null : comment.getParentComment().getId(),
        comment.getPosts() == null ? null : comment.getPosts().getId(),
        UserMapper.toDto(comment.getUsers(), false));
  }

  public static CommentPageResponseDTO toDto(Page<Comment> comments) {
    CommentPageResponseDTO dto = PageMapper.toDto(comments, new CommentPageResponseDTO());
    dto.setContent(comments.getContent().stream().map(CommentMapper::toDto).toList());
    return dto;
  }
}
