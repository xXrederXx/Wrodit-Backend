package ch.bbcag.wrodit.controllers;

import ch.bbcag.wrodit.dto.response.backup.BackupResponseDTO;
import ch.bbcag.wrodit.services.BackupService;
import ch.bbcag.wrodit.util.annotation.ApiResponses.ApiAuthResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BackupController.PATH)
public class BackupController {
  public static final String PATH = "/backup";
  private final BackupService service;

  public BackupController(BackupService service) {
    this.service = service;
  }

  @GetMapping("")
  @Operation(summary = "Export the whole database as backup data")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Backup export generated",
            content = @Content(schema = @Schema(implementation = BackupResponseDTO.class)))
      })
  @ApiAuthResponses
  public ResponseEntity<BackupResponseDTO> getBackup() {
    return ResponseEntity.ok(service.getBackup());
  }
}
