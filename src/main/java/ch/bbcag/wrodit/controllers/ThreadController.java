package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.ThreadRequestDTO;
import ch.bbcag.wrodit.dto.response.PostResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadPageResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadResponseDTO;
import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.mapper.ThreadMapper;
import ch.bbcag.wrodit.security.SecurityConstants;
import ch.bbcag.wrodit.services.ThreadService;
import ch.bbcag.wrodit.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ThreadController.PATH)
public class ThreadController {
  public static final String PATH = "/threads";
  private final ThreadService service;
  private final UserService userService;

  public ThreadController(ThreadService service, UserService userService) {
    this.service = service;
    this.userService = userService;
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a thread")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Thread found",
            content = @Content(schema = @Schema(implementation = ThreadResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Thread was not found", content = @Content)
      })
  public ResponseEntity<?> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(ThreadMapper.toDTO(service.findById(id)));
  }

  @GetMapping("/")
  @Operation(summary = "Get multiple Threads")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Page generated",
            content = @Content(schema = @Schema(implementation = ThreadPageResponseDTO.class))),
      })
  public ResponseEntity<?> getAllThreads(Pageable page) {
    return ResponseEntity.ok(ThreadMapper.toPageDto(service.paginatedThreads(page)));
  }

  @GetMapping("/userfeed")
  @Operation(summary = "Get all threads a user is subscribed to")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Page generated",
            content = @Content(schema = @Schema(implementation = ThreadPageResponseDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The user was unauthorized",
            content = @Content)
      })
  public ResponseEntity<?> getUserThreads(
      Pageable page,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_ID) Integer authId,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_PASSWORD) String authPasswd) {
    User user = userService.throwIfUnauthorized(authId, authPasswd);
    return ResponseEntity.ok(ThreadMapper.toPageDto(service.paginatedThreadsByUser(user, page)));
  }

  @PostMapping("/")
  @Operation(summary = "Create a thread")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Thread was created",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The user was unauthorized",
            content = @Content)
      })
  public ResponseEntity<?> postThread(
      @Valid @RequestBody ThreadRequestDTO dto,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_ID) Integer authId,
      @RequestHeader(name = SecurityConstants.AUTH_HEADER_PASSWORD) String authPasswd) {
    userService.throwIfUnauthorized(authId, authPasswd);
    ThreadResponseDTO responseDTO = ThreadMapper.toDTO(service.save(ThreadMapper.fromDto(dto)));
    return ResponseEntity.created(URI.create(PATH + "/" + responseDTO.id())).body(responseDTO);
  }
}
