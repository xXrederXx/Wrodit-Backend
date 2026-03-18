package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.response.ThreadPageResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadResponseDTO;
import ch.bbcag.wrodit.entitys.Thread;
import org.springframework.data.domain.Page;

public class ThreadMapper {
  public static ThreadResponseDTO toDTO(Thread therad) {
    return new ThreadResponseDTO(
        therad.getId(), therad.getName(), therad.getDescription(), therad.getCreatedAt());
  }

  public static ThreadPageResponseDTO toPageDto(Page<Thread> page) {
    ThreadPageResponseDTO dto = PageMapper.toDto(page, new ThreadPageResponseDTO());
    dto.setContent(page.getContent().stream().map(ThreadMapper::toDTO).toList());
    return dto;
  }
}
