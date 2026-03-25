package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.request.ThreadRequestDTO;
import ch.bbcag.wrodit.dto.response.ThreadPageResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadResponseDTO;
import ch.bbcag.wrodit.entitys.Thread;
import org.springframework.data.domain.Page;

public class ThreadMapper {
  public static Thread fromDto(ThreadRequestDTO dto) {
    Thread thread = new Thread();
    thread.setName(dto.name());
    thread.setDescription(dto.description());
    return thread;
  }

  public static ThreadResponseDTO toDto(Thread therad) {
    return new ThreadResponseDTO(
        therad.getId(), therad.getName(), therad.getDescription(), therad.getCreatedAt());
  }

  public static ThreadPageResponseDTO toDto(Page<Thread> page) {
    ThreadPageResponseDTO dto = PageMapper.toDto(page, new ThreadPageResponseDTO());
    dto.setContent(page.getContent().stream().map(ThreadMapper::toDto).toList());
    return dto;
  }
}
