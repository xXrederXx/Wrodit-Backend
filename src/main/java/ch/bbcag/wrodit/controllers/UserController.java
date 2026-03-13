package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.UserRequestDTO;
import ch.bbcag.wrodit.dto.response.UserResponseDTO;
import ch.bbcag.wrodit.dto.response.UserValidateResponseDTO;
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
    return ResponseEntity.ok(UserMapper.toUserDto(service.findById(id), false));
  }

  @GetMapping("/{id}/all")
  @Operation(summary = "Get all user data")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Person found",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Person was not found",
            content = @Content),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized, wrong id or WI-Auth-Passwd",
            content = @Content)
      })
  public ResponseEntity<?> getUserWholeById(
      @Parameter(description = "Id of the user to get, must be you") @PathVariable Integer id,
      @Parameter(description = "The password for the account information you are trying to get")
          @RequestHeader(name = "WI-Auth-Passwd")
          String authPassword) {
    service.throwIfUnauthorized(id, authPassword);
    return ResponseEntity.ok(UserMapper.toUserDto(service.findById(id), true));
  }

  @PostMapping("/validate")
  @Operation(summary = "Check if username and password match")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "If the data was correct",
            content = @Content(schema = @Schema(implementation = UserValidateResponseDTO.class))),
      })
  public ResponseEntity<?> postValidateUser(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The userdata you want to validate")
          @RequestBody
          UserRequestDTO dto) {
    return ResponseEntity.ok(
        UserMapper.toValidateDTO(service.checkAuthorization(dto.username(), dto.password())));
  }
}
