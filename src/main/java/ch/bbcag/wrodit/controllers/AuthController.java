package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.request.AuthRequestDTO;
import ch.bbcag.wrodit.dto.response.AuthResponseDTO;
import ch.bbcag.wrodit.dto.response.JWTResponseDTO;
import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.mapper.AuthMapper;
import ch.bbcag.wrodit.security.JWTGenerator;
import ch.bbcag.wrodit.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthController.PATH)
public class AuthController {
  public static final String PATH = "/auth";
  private final UserService service;
  private final AuthenticationManager authenticationManager;

  public AuthController(UserService service, AuthenticationManager authenticationManager) {
    this.service = service;
    this.authenticationManager = authenticationManager;
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

  @PostMapping("/signin")
  @Operation(summary = "Signin for the JWT")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "JWT generated successfully",
            content = @Content(schema = @Schema(implementation = JWTResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
      })
  public ResponseEntity<?> signIn(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The user to sign in")
          @Valid
          @RequestBody
          AuthRequestDTO dto) {

    try {
      String username = dto.username();
      User user = service.findByUsername(username);

      Authentication token = new UsernamePasswordAuthenticationToken(username, dto.password());

      if (authenticationManager.authenticate(token).isAuthenticated()) {
        return ResponseEntity.ok(
            new JWTResponseDTO(JWTGenerator.generateJwtToken(user.getId(), username), username, user.getId()));
      }
    } catch (EntityNotFoundException | BadCredentialsException ex) {
      throw new AuthorizationDeniedException(ex.getMessage());
    }
    throw new AuthorizationDeniedException("Unauthorized");
  }
}
