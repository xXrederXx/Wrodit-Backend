package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.ThreadRequestDTO;
import ch.bbcag.wrodit.dto.response.PostResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadPageResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadResponseDTO;
import ch.bbcag.wrodit.mapper.ThreadMapper;
import ch.bbcag.wrodit.services.ThreadService;
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
@RequestMapping(ThreadController.PATH)
public class ThreadController {
  public static final String PATH = "/threads";
  private final ThreadService service;

  public ThreadController(ThreadService service) {
    this.service = service;
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
  @ApiAuthResponses
  public ResponseEntity<?> getThreadById(
      @Parameter(description = "The threads id you want to get") @PathVariable Integer id) {
    return ResponseEntity.ok(ThreadMapper.toDTO(service.findById(id)));
  }

  @GetMapping("/")
  @Operation(summary = "Get multiple Threads")
  @ApiResponse(
      responseCode = "200",
      description = "Thread page generated",
      content = @Content(schema = @Schema(implementation = ThreadPageResponseDTO.class)))
  @ApiAuthResponses
  public ResponseEntity<?> getPaginatedThreads(Pageable page) {
    return ResponseEntity.ok(ThreadMapper.toPageDto(service.paginatedThreads(page)));
  }

  @GetMapping("/userfeed")
  @Operation(summary = "Get all threads a user is subscribed to")
  @ApiResponse(
      responseCode = "200",
      description = "Thread page generated",
      content = @Content(schema = @Schema(implementation = ThreadPageResponseDTO.class)))
  @ApiAuthResponses
  public ResponseEntity<?> getUserThreads(
      Pageable page, @AuthenticationPrincipal(expression = "claims['userId']") Integer userId) {

    return ResponseEntity.ok(ThreadMapper.toPageDto(service.paginatedThreadsByUser(userId, page)));
  }

  @PostMapping("/")
  @Operation(summary = "Create a thread")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Thread was created",
            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The thread has conflicting data",
            content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> postThread(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The thread you would like to create")
          @Valid
          @RequestBody
          ThreadRequestDTO dto) {
    ThreadResponseDTO responseDTO = ThreadMapper.toDTO(service.save(ThreadMapper.fromDto(dto)));
    return ResponseEntity.created(URI.create(PATH + "/" + responseDTO.id())).body(responseDTO);
  }
}
