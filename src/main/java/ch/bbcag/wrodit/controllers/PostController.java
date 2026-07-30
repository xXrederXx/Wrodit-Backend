package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.PostCreateRequestDTO;
import ch.bbcag.wrodit.dto.request.PostRequestDTO;
import ch.bbcag.wrodit.dto.request.VoteRequestDTO;
import ch.bbcag.wrodit.dto.response.PostPageResponseDTO;
import ch.bbcag.wrodit.dto.response.PostResponseDTO;
import ch.bbcag.wrodit.dto.response.VoteResponseDTO;
import ch.bbcag.wrodit.entities.Post;
import ch.bbcag.wrodit.mapper.PostMapper;
import ch.bbcag.wrodit.services.PostService;
import ch.bbcag.wrodit.services.PostVoteService;
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
import java.util.concurrent.TimeUnit;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PostController.PATH)
public class PostController {
  public static final String PATH = "/posts";
  private final PostService service;
  private final PostVoteService postVoteService;

  public PostController(PostService service, PostVoteService postVoteService) {
    this.service = service;
    this.postVoteService = postVoteService;
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a post")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Post found",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Post was not found", content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> getPostById(
      @Parameter(description = "The id of the post you want") @PathVariable Integer id) {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
        .body(PostMapper.toDto(service.getPostById(id)));
  }

  @GetMapping("/")
  @Operation(summary = "Get multiple Posts")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Page generated",
            content = @Content(schema = @Schema(implementation = PostPageResponseDTO.class))),
      })
  @ApiAuthResponses
  public ResponseEntity<?> getPaginatedPosts(
      Pageable page,
      @Parameter(description = "The users id which is filtered with")
          @RequestParam(required = false)
          Integer user,
      @Parameter(description = "The threads id which is filtered with")
          @RequestParam(required = false)
          Integer thread) {
    return ResponseEntity.ok(PostMapper.toDto(service.getPaginatedPosts(user, thread, page)));
  }

  @PostMapping("/")
  @Operation(summary = "Create a new post")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Post was created successfully",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "Post could not be created, due to a conflict in data",
            content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> postPost(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The post you would like to create")
          @Valid
          @RequestBody
          PostCreateRequestDTO post,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    Post responsePost =
        service.save(PostMapper.fromDto(post), userId == null ? null : userId.intValue());
    return ResponseEntity.created(URI.create(PATH + "/" + responsePost.getId()))
        .body(PostMapper.toDto(responsePost));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update a post")
  @ApiResponse(
      responseCode = "200",
      description = "Post was Updated",
      content = @Content(schema = @Schema(implementation = PostResponseDTO.class)))
  @ApiAuthResponses
  public ResponseEntity<?> patchPost(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The attributes you would like to change")
          @RequestBody
          PostRequestDTO dto,
      @Parameter(description = "The post id which you want to update") @PathVariable Integer id,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {

    return ResponseEntity.ok(
        PostMapper.toDto(
            service.update(
                PostMapper.fromDto(dto), id, userId == null ? null : userId.intValue())));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a post by its id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Post was deleted successfully",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Post was not found", content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> deletePost(
      @Parameter(description = "The posts id which you want to delete") @PathVariable Integer id,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    service.deletePostById(id, userId == null ? null : userId.intValue());
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
            postVoteService.find(id, userId == null ? null : userId.intValue()).getVote()));
  }

  @PutMapping("/{id}/vote")
  @Operation(summary = "Apply a vote on a post")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Update Successful", content = @Content),
        @ApiResponse(responseCode = "404", description = "Post was not found", content = @Content)
      })
  @ApiAuthResponses
  @Api400Response
  public ResponseEntity<?> votePost(
      @Parameter(description = "The posts id which you want to delete") @PathVariable Integer id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The vote you would like to make")
          @Valid
          @RequestBody
          VoteRequestDTO dto,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    postVoteService.update(dto.vote(), id, userId == null ? null : userId.intValue());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/vote")
  @Operation(summary = "Delete a vote on a post")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Delete Successful", content = @Content),
        @ApiResponse(responseCode = "404", description = "Post was not found", content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> voteDelete(
      @Parameter(description = "The posts id which you want to delete") @PathVariable Integer id,
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    postVoteService.deleteById(userId == null ? null : userId.intValue(), id);
    return ResponseEntity.noContent().build();
  }
}
