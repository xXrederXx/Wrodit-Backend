package ch.bbcag.wrodit.dto.response;

import java.util.List;
import java.util.Objects;

public class PostPageResponseDTO extends PageableResponseDTO {
  private List<PostResponseDTO> content;

  public List<PostResponseDTO> getContent() {
    return content;
  }

  public void setContent(List<PostResponseDTO> content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PostPageResponseDTO that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), content);
  }

  @Override
  public String toString() {
    return "ThreadPageResponseDTO{" + "content=" + content + "} " + super.toString();
  }
}
