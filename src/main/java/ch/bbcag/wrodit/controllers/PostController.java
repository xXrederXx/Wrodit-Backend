package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.PostCreateRequestDTO;
import ch.bbcag.wrodit.dto.request.PostRequestDTO;
import ch.bbcag.wrodit.dto.response.PostPageResponseDTO;
import ch.bbcag.wrodit.dto.response.PostResponseDTO;
import ch.bbcag.wrodit.entitys.Post;
import ch.bbcag.wrodit.mapper.PostMapper;
import ch.bbcag.wrodit.services.PostService;
import ch.bbcag.wrodit.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PostController.PATH)
public class PostController {
  public static final String PATH = "/posts";
  private final PostService service;
  private final UserService userService;

  public PostController(PostService service, UserService userService) {
    this.service = service;
    this.userService = userService;
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
  public ResponseEntity<?> getPostById(@PathVariable Integer id) {
    return ResponseEntity.ok(PostMapper.toDto(service.getPostById(id)));
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
  public ResponseEntity<?> getPaginatedPosts(
      Pageable page,
      @RequestParam(required = false) Integer user,
      @RequestParam(required = false) Integer thread) {
    return ResponseEntity.ok(PostMapper.toPageDto(service.getPaginatedPosts(user, thread, page)));
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
            description = "User could not be created, username already in use",
            content = @Content)
      })
  public ResponseEntity<?> postPost(@RequestBody PostCreateRequestDTO post) {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer userId = jwt.getClaim("userId");
    Post responsePost = service.save(PostMapper.fromDto(post), userId);
    return ResponseEntity.created(URI.create(PATH + "/" + responsePost.getId()))
        .body(PostMapper.toDto(responsePost));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update a post")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Post was Updated",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The user was unauthorized",
            content = @Content)
      })
  public ResponseEntity<?> patchPost(@RequestBody PostRequestDTO dto, @PathVariable Integer id) {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer userId = jwt.getClaim("userId");
    return ResponseEntity.ok(PostMapper.toDto(service.update(PostMapper.fromDto(dto), id, userId)));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a tag by its id")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Tag was deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tag was not found", content = @Content)
      })
  public ResponseEntity<?> deletePost(@PathVariable Integer id) {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer userId = jwt.getClaim("userId");
    service.deletePostById(id, userId);
    return ResponseEntity.noContent().build();
  }
}
