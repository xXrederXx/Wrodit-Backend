package ch.bbcag.wrodit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entitys.Thread;
import ch.bbcag.wrodit.repos.ThreadRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

class ThreadServiceTest {
  private ThreadRepository mockRepo;
  private ThreadService threadService;

  private Thread mockThread;
  private Page<Thread> mockThreadPage;

  @BeforeEach
  void setup() {
    mockRepo = Mockito.mock(ThreadRepository.class);
    threadService = new ThreadService(mockRepo);

    mockThread = TestingUtil.generateThreads(1)[0];
    mockThreadPage = new PageImpl<>(Arrays.stream(TestingUtil.generateThreads(10)).toList());
  }

  @Test
  void checkFindById_whenValidId_thenReturn() {
    when(mockRepo.findById(anyInt())).thenReturn(Optional.of(mockThread));

    assertEquals(mockThread, threadService.findById(1));
  }

  @Test
  void checkFindById_whenInvalidId_thenThrow() {
    when(mockRepo.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> threadService.findById(1));
  }

  @Test
  void checkFindPaginated_whenValid_thenSuccess() {
    when(mockRepo.findAll(any(Pageable.class))).thenReturn(mockThreadPage);

    assertEquals(threadService.paginatedThreads(PageRequest.of(0, 10)), mockThreadPage);
  }

  @Test
  void checkFindPaginatedByUser_whenValid_thenSuccess() {
    when(mockRepo.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(mockThreadPage);

    assertEquals(threadService.paginatedThreadsByUser(1, PageRequest.of(0, 10)), mockThreadPage);
  }

  @Test
  void checkSave_whenValidThread_thenSuccess() {
    Thread thread = TestingUtil.generateThreads(1)[0];

    when(mockRepo.save(any(Thread.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Thread result = threadService.save(thread);

    Assertions.assertTrue(
        OffsetDateTime.now().toEpochSecond() - result.getCreatedAt().toEpochSecond()
            < TestingUtil.MAX_TIME_CHECK_DIFF);
    verify(mockRepo).save(thread);
  }
}
