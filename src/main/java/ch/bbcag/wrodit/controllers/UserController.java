package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.response.UserResponseDTO;
import ch.bbcag.wrodit.mapper.UserMapper;
import ch.bbcag.wrodit.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserController.PATH)
public class UserController {
  public static final String PATH = "/users";
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Person found",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Person was not found", content = @Content)
      })
  public ResponseEntity<?> getUserById(
      @Parameter(description = "Id of the user to get") @PathVariable Integer id) {
    return ResponseEntity.ok(UserMapper.toDto(service.findById(id), false));
  }

  @GetMapping("/{id}/all")
  @Operation(summary = "Get all user data")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Person found",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Person was not found", content = @Content)
      })
  public ResponseEntity<?> getUserWholeById(
      @Parameter(description = "Id of the user to get") @PathVariable Integer id,
      @RequestHeader(name = "WI-Auth-ID") Integer authID,
      @RequestHeader(name = "WI-Auth-Passwd") String authPassword) {
    service.throwIfUnauthorized(authID, authPassword);
    return ResponseEntity.ok(UserMapper.toDto(service.findById(id), true));
  }
}
