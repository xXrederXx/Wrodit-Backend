package ch.bbcag.wrodit.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entitys.Thread;
import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.services.ThreadService;
import ch.bbcag.wrodit.util.URIHelper;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ThreadController.class)
@AutoConfigureMockMvc(addFilters = false)
class ThreadControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private ThreadService threadService;

  private static Thread mockThread;
  private static Page<Thread> mockThreadPage;
  private static User mockUser;

  @BeforeAll
  static void init() {
    Thread[] threads = TestingUtil.generateThreads(10);
    mockThreadPage = new PageImpl<>(Arrays.stream(threads).toList(), PageRequest.of(0, 10), 10);
    mockThread = threads[0];

    mockUser = TestingUtil.generateUser();
  }

  @Test
  void checkGetThreads_whenThreadExists_thenSuccess() throws Exception {
    Mockito.when(threadService.paginatedThreads(any(Pageable.class))).thenReturn(mockThreadPage);

    mockMvc.perform(get(URIHelper.join(ThreadController.PATH, ""))).andExpect(status().isOk());
  }

  @Test
  void checkGetThreads_whenNoThreadExists_thenSuccess() throws Exception {
    Mockito.when(threadService.paginatedThreads(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of()));

    mockMvc.perform(get(URIHelper.join(ThreadController.PATH, ""))).andExpect(status().isOk());
  }

  @Test
  void checkPostThreads_whenValidThread_thenCreated() throws Exception {
    Mockito.when(threadService.save(any(Thread.class))).thenReturn(mockThread);

    mockMvc
        .perform(
            post(URIHelper.join(ThreadController.PATH, ""))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    String.format(
                        """
                        {
                            "name":"%s",
                            "description":"%s"
                        }
                        """,
                        mockThread.getName(), mockThread.getDescription())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is(mockThread.getName())))
        .andExpect(jsonPath("$.description", is(mockThread.getDescription())))
        .andExpect(jsonPath("$.createdAt", is(mockThread.getCreatedAt().toString())));
  }

  @Test
  void checkPostThreads_whenInvalidThread_thenBadRequest() throws Exception {

    mockMvc
        .perform(
            post(URIHelper.join(ThreadController.PATH, ""))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    String.format(
                        """
                        {
                            "not_name":"%s",
                            "description":"%s"
                        }
                        """,
                        mockThread.getName(), mockThread.getDescription())))
        .andExpect(status().isBadRequest());
  }

  @Test
  void checkGetById_whenValidUser_thenIsReturned() throws Exception {
    Mockito.when(threadService.findById(any(Integer.class))).thenReturn(mockThread);

    mockMvc
        .perform(get(URIHelper.join(ThreadController.PATH, "1")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(mockThread.getId())))
        .andExpect(jsonPath("$.name", is(mockThread.getName())))
        .andExpect(jsonPath("$.description", is(mockThread.getDescription())))
        .andExpect(jsonPath("$.createdAt", is(mockThread.getCreatedAt().toString())));
    // .andExpect(content().string(not(containsString("\"email\"")))); Uncomment once Issue Resolves
  }

  @Test
  void checkGetById_whenNoThread_then404Returned() throws Exception {
    Mockito.when(threadService.findById(any(Integer.class)))
        .thenThrow(EntityNotFoundException.class);

    mockMvc
        .perform(get(URIHelper.join(ThreadController.PATH, "1")))
        .andExpect(status().isNotFound());
  }

  @Test
  void checkGetUserfeedThreads_whenThreadExists_thenSuccess() throws Exception {
    Mockito.when(threadService.paginatedThreadsByUser(any(), any(Pageable.class)))
        .thenReturn(mockThreadPage);

    mockMvc
        .perform(
            get(URIHelper.join(ThreadController.PATH, "userfeed"))
                .with(jwt().jwt(jwt -> jwt.claim("userId", mockUser.getId()))))
        .andExpect(status().isOk());
  }

  @Test
  void checkGetUserfeedThreads_whenNoThreadExists_thenSuccess() throws Exception {
    Mockito.when(threadService.paginatedThreadsByUser(any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of()));

    mockMvc
        .perform(
            get(URIHelper.join(ThreadController.PATH, "userfeed"))
                .with(jwt().jwt(jwt -> jwt.claim("userId", mockUser.getId()))))
        .andExpect(status().isOk());
  }
}
