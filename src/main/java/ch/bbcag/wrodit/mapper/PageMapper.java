package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.response.PageableResponseDTO;
import org.springframework.data.domain.Page;

public class PageMapper {
  private PageMapper() {
    // hide ctor
  }

  public static <T, U extends PageableResponseDTO> U toDto(Page<T> page, U dto) {
    dto.setPage(page.getNumber());
    dto.setFirst(page.isFirst());
    dto.setLast(page.isLast());
    dto.setSize(page.getSize());
    dto.setTotalPages(page.getTotalPages());
    dto.setTotalElements(page.getTotalElements());
    return dto;
  }
}
