package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.response.ThreadResponseDTO;
import ch.bbcag.wrodit.mapper.ThreadMapper;
import ch.bbcag.wrodit.services.ThreadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
            content = @Content(schema = @Schema(implementation = Page.class))),
      })
  public ResponseEntity<?> getAllThreads(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
    return ResponseEntity.ok(
        service.paginatedThreads(page, pageSize, Sort.unsorted()).map(ThreadMapper::toDTO));
  }
}
