package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.request.PostCreateRequestDTO;
import ch.bbcag.wrodit.dto.request.PostRequestDTO;
import ch.bbcag.wrodit.dto.response.PostPageResponseDTO;
import ch.bbcag.wrodit.dto.response.PostResponseDTO;
import ch.bbcag.wrodit.entitys.Post;
import ch.bbcag.wrodit.entitys.PostsVote;
import ch.bbcag.wrodit.entitys.Thread;
import org.springframework.data.domain.Page;

public class PostMapper {
  private PostMapper() {
    // hide ctor
  }

  public static PostResponseDTO toDto(Post post) {
    return new PostResponseDTO(
        post.getTitle(),
        post.getContent(),
        post.getPostsPostsVotes().stream().mapToInt(PostsVote::getVote).sum(),
        post.getCreatedAt(),
        post.getUsers().getId(),
        post.getThreads().getId());
  }

  public static PostPageResponseDTO toDto(Page<Post> page) {
    PostPageResponseDTO dto = PageMapper.toDto(page, new PostPageResponseDTO());
    dto.setContent(page.getContent().stream().map(PostMapper::toDto).toList());
    return dto;
  }

  public static Post fromDto(PostRequestDTO dto) {
    Post post = new Post();
    post.setTitle(dto.title());
    post.setContent(dto.content());
    return post;
  }

  public static Post fromDto(PostCreateRequestDTO dto) {
    Post post = new Post();
    post.setTitle(dto.title());
    post.setContent(dto.content());
    post.setThreads(dto.threadId() == null ? null : new Thread(dto.threadId()));
    return post;
  }
}
