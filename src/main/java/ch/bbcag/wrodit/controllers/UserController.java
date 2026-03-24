package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.response.UserResponseDTO;
import ch.bbcag.wrodit.mapper.UserMapper;
import ch.bbcag.wrodit.services.UserService;
import ch.bbcag.wrodit.util.annotation.ApiResponses.Api401Response;
import ch.bbcag.wrodit.util.annotation.ApiResponses.ApiAuthResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "User was not found", content = @Content)
      })
  @ApiAuthResponses
  public ResponseEntity<?> getUserById(
      @Parameter(description = "Id of the user to get") @PathVariable Integer id) {
    return ResponseEntity.ok(UserMapper.toUserDto(service.findById(id), false));
  }

  @GetMapping("/self")
  @Operation(summary = "Get all your data")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "User was not found", content = @Content)
      })
  @Api401Response
  public ResponseEntity<?> getWholeUserById(
      @AuthenticationPrincipal(expression = "claims['userId']") Long userId) {
    return ResponseEntity.ok(
        UserMapper.toUserDto(service.findById(userId == null ? null : userId.intValue()), true));
  }
}
