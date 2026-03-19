package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.AuthRequestDTO;
import ch.bbcag.wrodit.dto.response.AuthResponseDTO;
import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.mapper.AuthMapper;
import ch.bbcag.wrodit.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthController.PATH)
public class AuthController {
  public static final String PATH = "/auth";
  private final UserService service;

  public AuthController(UserService service) {
    this.service = service;
  }

  @PostMapping("/signup")
  @Operation(summary = "Create a new user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "User was created successfully",
            content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
        @ApiResponse(
            responseCode = "409",
            description = "User could not be created, username already in use",
            content = @Content)
      })
  public ResponseEntity<?> signUp(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The user to register")
          @Valid
          @RequestBody
          AuthRequestDTO dto) {
    User auth = AuthMapper.fromDTO(dto);
    User savedAuth = service.insert(auth);
    return ResponseEntity.created(URI.create(UserController.PATH + "/" + savedAuth.getId()))
        .body(AuthMapper.toDTO(savedAuth));
  }
}
