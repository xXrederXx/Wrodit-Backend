package ch.bbcag.wrodit.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.dto.request.ThreadRequestDTO;
import ch.bbcag.wrodit.dto.response.ThreadPageResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadResponseDTO;
import ch.bbcag.wrodit.entities.Thread;
import ch.bbcag.wrodit.mapper.ThreadMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

class ThreadMapperTest {
  @Test
  void checkToDto_whenValidEntity_thenDto() {
    Thread mockThread = TestingUtil.generateThreads(1)[0];
    ThreadResponseDTO dto = ThreadMapper.toDto(mockThread);

    assertEquals(mockThread.getId(), dto.id());
    assertEquals(mockThread.getName(), dto.name());
    assertEquals(mockThread.getDescription(), dto.description());
    assertEquals(mockThread.getCreatedAt(), dto.createdAt());
  }

  @Test
  void checkToDto_whenValidPage_thenDto() {
    Page<Thread> mockThreadPage =
        new PageImpl<>(Arrays.stream(TestingUtil.generateThreads(10)).toList());
    ThreadPageResponseDTO dto = ThreadMapper.toDto(mockThreadPage);

    assertEquals(10, dto.getContent().size());
    assertNotNull(dto.getPage());
    assertNotNull(dto.getFirst());
    assertNotNull(dto.getLast());
    assertNotNull(dto.getSize());
    assertNotNull(dto.getTotalPages());
    assertNotNull(dto.getTotalElements());
  }

  @Test
  void checkFromDto_whenValidEntity_thenDto() {
    ThreadRequestDTO dto = generateDto();
    Thread thread = ThreadMapper.fromDto(dto);

    assertEquals(dto.name(), thread.getName());
    assertEquals(dto.description(), thread.getDescription());
  }

  private ThreadRequestDTO generateDto() {
    return new ThreadRequestDTO("Name", "Description");
  }
}
