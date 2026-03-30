package ch.bbcag.wrodit.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.dto.request.PostCreateRequestDTO;
import ch.bbcag.wrodit.dto.request.PostRequestDTO;
import ch.bbcag.wrodit.dto.response.PostPageResponseDTO;
import ch.bbcag.wrodit.dto.response.PostResponseDTO;
import ch.bbcag.wrodit.entities.Post;
import ch.bbcag.wrodit.entities.PostsVote;
import ch.bbcag.wrodit.mapper.PostMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

class PostMapperTest {
  @Test
  void checkToDto_whenValidEntity_thenDto() {
    Post mockPost = TestingUtil.generatePosts(1)[0];
    PostResponseDTO dto = PostMapper.toDto(mockPost);

    assertEquals(mockPost.getId(), dto.id());
    assertEquals(mockPost.getTitle(), dto.title());
    assertEquals(mockPost.getContent(), dto.content());
    assertEquals(mockPost.getUsers().getId(), dto.userId());
    assertEquals(mockPost.getThreads().getId(), dto.threadId());
    assertEquals(mockPost.getPostVotes().stream().mapToInt(PostsVote::getVote).sum(), dto.vote());
    assertEquals(mockPost.getCreatedAt(), dto.createdAt());
  }

  @Test
  void checkToDto_whenValidPage_thenDto() {
    Page<Post> mockPostPage = new PageImpl<>(Arrays.stream(TestingUtil.generatePosts(10)).toList());
    PostPageResponseDTO dto = PostMapper.toDto(mockPostPage);

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
    PostRequestDTO dto = generateDto();
    Post post = PostMapper.fromDto(dto);

    assertEquals(dto.title(), post.getTitle());
    assertEquals(dto.content(), post.getContent());
  }

  @Test
  void checkFromCreateDto_whenValidEntity_thenDto() {
    PostCreateRequestDTO dto = generateCreateDto();
    Post post = PostMapper.fromDto(dto);

    assertEquals(dto.title(), post.getTitle());
    assertEquals(dto.content(), post.getContent());
    assertEquals(dto.threadId(), post.getThreads().getId());
  }

  private PostRequestDTO generateDto() {
    return new PostRequestDTO("Title", "Content");
  }

  private PostCreateRequestDTO generateCreateDto() {
    return new PostCreateRequestDTO("Title", "Content", 1);
  }
}
