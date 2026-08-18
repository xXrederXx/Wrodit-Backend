package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.request.ThreadRequestDTO;
import ch.bbcag.wrodit.dto.response.PostResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadPageResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadResponseDTO;
import ch.bbcag.wrodit.dto.response.ThreadStatsResponseDTO;
import ch.bbcag.wrodit.dto.response.UserResponseDTO;
import ch.bbcag.wrodit.entities.Thread;
import ch.bbcag.wrodit.entities.User;

import java.util.List;

import org.springframework.data.domain.Page;

public class ThreadMapper {
  public static Thread fromDto(ThreadRequestDTO dto) {
    Thread thread = new Thread();
    thread.setName(dto.name());
    thread.setDescription(dto.description());
    return thread;
  }

  public static ThreadResponseDTO toDto(Thread thread) {
    return new ThreadResponseDTO(
        thread.getId(), thread.getName(), thread.getDescription(), thread.getCreatedAt());
  }

  public static ThreadStatsResponseDTO toDto(Thread thread, Integer numberPosts, Integer numActiveUsers,
      List<User> activeUsers, PostResponseDTO lastPost) {
    return new ThreadStatsResponseDTO(
        thread.getId(), thread.getName(), thread.getDescription(), thread.getCreatedAt(), numberPosts, numActiveUsers,
        activeUsers.stream().map((u) -> UserMapper.toDto(u, false)).toList(), lastPost);
  }

  public static ThreadPageResponseDTO toDto(Page<Thread> page) {
    ThreadPageResponseDTO dto = PageMapper.toDto(page, new ThreadPageResponseDTO());
    dto.setContent(page.getContent().stream().map(ThreadMapper::toDto).toList());
    return dto;
  }
}
