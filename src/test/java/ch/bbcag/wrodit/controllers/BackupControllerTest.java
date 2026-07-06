package ch.bbcag.wrodit.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.wrodit.dto.response.backup.BackupResponseDTO;
import ch.bbcag.wrodit.dto.response.backup.CommentBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.CommentVoteBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.PostBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.PostVoteBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.ThreadBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.UserBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.UserThreadBackupDTO;
import ch.bbcag.wrodit.services.BackupService;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BackupController.class)
@AutoConfigureMockMvc(addFilters = false)
class BackupControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private BackupService backupService;

  @Test
  void checkGetBackup_whenServiceReturnsBackup_thenIsReturned() throws Exception {
    BackupResponseDTO backup =
        new BackupResponseDTO(
            List.of(
                new UserBackupDTO(
                    1,
                    "alice@example.com",
                    "alice",
                    "hash",
                    null,
                    OffsetDateTime.parse("2024-01-01T00:00:00+00:00"))),
            List.of(
                new ThreadBackupDTO(
                    1,
                    "General",
                    "desc",
                    "banner",
                    "icon",
                    OffsetDateTime.parse("2024-01-01T00:00:00+00:00"))),
            List.of(new UserThreadBackupDTO(1, 1)),
            List.of(
                new PostBackupDTO(
                    1, "Hello", "world", 1, 1, OffsetDateTime.parse("2024-01-01T00:00:00+00:00"))),
            List.of(
                new CommentBackupDTO(
                    1, 1, "Nice", OffsetDateTime.parse("2024-01-01T00:00:00+00:00"), null, 1)),
            List.of(new PostVoteBackupDTO(1, 1, 1)),
            List.of(new CommentVoteBackupDTO(1, 1, -1)));

    when(backupService.getBackup()).thenReturn(backup);

    mockMvc
        .perform(get("/backup"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.users[0].id", is(1)))
        .andExpect(jsonPath("$.users[0].username", is("alice")))
        .andExpect(jsonPath("$.threads[0].name", is("General")))
        .andExpect(jsonPath("$.usersThreads[0].users_id", is(1)))
        .andExpect(jsonPath("$.posts[0].title", is("Hello")))
        .andExpect(jsonPath("$.comments[0].content", is("Nice")))
        .andExpect(jsonPath("$.postVotes[0].vote", is(1)))
        .andExpect(jsonPath("$.commentVotes[0].vote", is(-1)));
  }
}
