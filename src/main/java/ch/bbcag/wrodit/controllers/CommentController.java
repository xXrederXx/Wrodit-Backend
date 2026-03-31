package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.CommentCreateDTO;
import ch.bbcag.wrodit.dto.request.CommentRequestDTO;
import ch.bbcag.wrodit.dto.request.VoteRequestDTO;
import ch.bbcag.wrodit.dto.response.CommentPageResponseDTO;
import ch.bbcag.wrodit.dto.response.CommentResponseDTO;
import ch.bbcag.wrodit.dto.response.VoteResponseDTO;
import ch.bbcag.wrodit.mapper.CommentMapper;
import ch.bbcag.wrodit.services.CommentService;
import ch.bbcag.wrodit.services.CommentVoteService;
import ch.bbcag.wrodit.util.annotation.ApiResponses.Api400Response;
import ch.bbcag.wrodit.util.annotation.ApiResponses.ApiAuthResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CommentController.PATH)
public class CommentController {
  public static final String PATH = "/comments";
  private final CommentService commentService;
  private final CommentVoteService commentVoteService;

  public CommentController(CommentService commentService, CommentVoteService commentVoteService) {
    this.commentService = commentService;
    this.commentVoteService = commentVoteService;
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a comment")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Comment found",
            content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Comment was not found",
            content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> getCommentById(
      @Parameter(description = "The id of the comment which you want") @PathVariable Integer id) {
    return ResponseEntity.ok(CommentMapper.toDto(commentService.getCommentById(id)));
  }

  @GetMapping("/")
  @Operation(summary = "Get multiple Comments")
  @ApiResponse(
      responseCode = "200",
      description = "Comment Page generated",
      content = @Content(schema = @Schema(implementation = CommentPageResponseDTO.class)))
  @ApiAuthResponses
  public ResponseEntity<?> getPagableComments(
      Pageable page,
      @RequestParam(required = false)
          @Parameter(description = "The post id which is used to filter")
          Integer post,
      @RequestParam(required = false)
          @Parameter(description = "The parent id which is used to filter")
          Integer parent) {
    return ResponseEntity.ok(
        CommentMapper.toDto(commentService.getPaginatedComments(page, post, parent)));
  }

  @PostMapping("/")
  @Operation(
      summary = "Create a comment",
      description =
          "This Endpoint is used to create a Comment. When creating a comment you need to pass the parentId " +
                  "(if the comment was made on another comment), or the postId (if the comment was made on a post)." +
                  " The other should be null")
  @ApiResponse(
      responseCode = "201",
      description = "Comment was created",
      content = @Content(schema = @Schema(implementation = CommentResponseDTO.class)))
  @ApiAuthResponses
  public ResponseEntity<?> postComment(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The comment you want to create")
          @Valid
          @RequestBody
          CommentCreateDTO commentCreateDTO,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    CommentResponseDTO responseDTO =
        CommentMapper.toDto(
            commentService.save(
                CommentMapper.fromDto(commentCreateDTO),
                userId == null ? null : userId.intValue()));
    return ResponseEntity.created(URI.create(PATH + "/" + responseDTO.id())).body(responseDTO);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update a comment")
  @ApiResponse(
      responseCode = "200",
      description = "comment was Updated",
      content = @Content(schema = @Schema(implementation = CommentResponseDTO.class)))
  @ApiAuthResponses
  public ResponseEntity<?> patchComment(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The attributes you would like to change")
          @RequestBody
          CommentRequestDTO dto,
      @Parameter(description = "The comment id you want to change") @PathVariable Integer id,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {

    return ResponseEntity.ok(
        CommentMapper.toDto(
            commentService.update(
                CommentMapper.fromDto(dto), id, userId == null ? null : userId.intValue())));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a comment by its id")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Comment was deleted successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "Comment was not found",
            content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> deleteComment(
      @Parameter(description = "The comments id you want to delete") @PathVariable Integer id,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {

    commentService.deletePostById(id, userId == null ? null : userId.intValue());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/vote")
  @Operation(summary = "Get your vote on a post")
  @ApiResponse(responseCode = "200", description = "Update Successful", content = @Content)
  @ApiAuthResponses
  @Api400Response
  public ResponseEntity<?> getVotePost(
      @Parameter(description = "The posts id which you want to delete") @PathVariable Integer id,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    return ResponseEntity.ok(
        new VoteResponseDTO(
            commentVoteService.find(id, userId == null ? null : userId.intValue()).getVote()));
  }

  @PutMapping("/{id}/vote")
  @Operation(summary = "Apply a vote on a comment")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Update Successful", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "Comment was not found",
            content = @Content)
      })
  @ApiAuthResponses
  @Api400Response
  public ResponseEntity<?> votePost(
      @Parameter(description = "The comment id which you want to update") @PathVariable Integer id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The vote you would like to make")
          @Valid
          @RequestBody
          VoteRequestDTO dto,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    commentVoteService.update(dto.vote(), id, userId == null ? null : userId.intValue());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/vote")
  @Operation(summary = "Delete a vote on a comment")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Delete Successful", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "Comment was not found",
            content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> voteDelete(
      @Parameter(description = "The comment id which you want to delete") @PathVariable Integer id,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    commentVoteService.deleteById(userId == null ? null : userId.intValue(), id);
    return ResponseEntity.noContent().build();
  }
}
