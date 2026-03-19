package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.CommentCreateDTO;
import ch.bbcag.wrodit.dto.request.CommentRequestDTO;
import ch.bbcag.wrodit.dto.response.CommentPageResponseDTO;
import ch.bbcag.wrodit.dto.response.CommentResponseDTO;
import ch.bbcag.wrodit.mapper.CommentMapper;
import ch.bbcag.wrodit.security.SecurityConstants;
import ch.bbcag.wrodit.services.CommentService;
import ch.bbcag.wrodit.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CommentController.PATH)
public class CommentController {
  public static final String PATH = "/comments";
  private final CommentService commentService;
  private final UserService userService;

  public CommentController(CommentService commentService, UserService userService) {
    this.commentService = commentService;
    this.userService = userService;
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a post")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Post found",
            content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Post was not found", content = @Content)
      })
  public ResponseEntity<?> getCommentById(@PathVariable Integer id) {
    return ResponseEntity.ok(CommentMapper.toDto(commentService.getCommentById(id)));
  }

  @GetMapping("/")
  @Operation(summary = "Get multiple Comments")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Page generated",
            content = @Content(schema = @Schema(implementation = CommentPageResponseDTO.class))),
      })
  public ResponseEntity<?> getPagableComments(
      Pageable page,
      @RequestParam(required = false) Integer post,
      @RequestParam(required = false) Integer parent) {
    return ResponseEntity.ok(
        CommentMapper.toDto(commentService.getPaginatedComments(page, post, parent)));
  }

  @PostMapping("/")
  @Operation(summary = "Create a comment")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Comment was created",
            content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The user was unauthorized",
            content = @Content)
      })
  public ResponseEntity<?> postComment(
      @RequestBody CommentCreateDTO commentCreateDTO,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_ID) Integer authId,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_PASSWORD) String authPasswd) {
    userService.throwIfUnauthorized(authId, authPasswd);
    CommentResponseDTO responseDTO =
        CommentMapper.toDto(commentService.save(CommentMapper.fromDto(commentCreateDTO), authId));
    return ResponseEntity.created(URI.create(PATH + "/" + responseDTO.id())).body(responseDTO);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update a comment")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "comment was Updated",
            content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The user was unauthorized",
            content = @Content)
      })
  public ResponseEntity<?> patchComment(
      @RequestBody CommentRequestDTO dto,
      @PathVariable Integer id,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_ID) Integer authId,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_PASSWORD) String authPasswd) {
    userService.throwIfUnauthorized(authId, authPasswd);
    return ResponseEntity.ok(
        CommentMapper.toDto(commentService.update(CommentMapper.fromDto(dto), id)));
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
  public ResponseEntity<?> deletePost(
      @PathVariable Integer id,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_ID) Integer authId,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_PASSWORD) String authPasswd) {
    userService.throwIfUnauthorized(authId, authPasswd);
    commentService.deletePostById(id, authId);
    return ResponseEntity.noContent().build();
  }
}
