package ch.bbcag.wrodit.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bbcag.wrodit.dto.response.PageableResponseDTO;
import ch.bbcag.wrodit.mapper.PageMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

class PageMapperTest {
  @Test
  void checkToDto_whenValidEntity_thenDto() {
    Page<String> mockPage = new PageImpl<>(List.of("a", "b", "c"));
    PageableResponseDTO dto = PageMapper.toDto(mockPage, new PageableResponseDTO());

    assertEquals(mockPage.getNumber(), dto.getPage());
    assertEquals(mockPage.isFirst(), dto.getFirst());
    assertEquals(mockPage.isLast(), dto.getLast());
    assertEquals(mockPage.getSize(), dto.getSize());
    assertEquals(mockPage.getTotalPages(), dto.getTotalPages());
    assertEquals(mockPage.getTotalElements(), dto.getTotalElements());
  }
}
