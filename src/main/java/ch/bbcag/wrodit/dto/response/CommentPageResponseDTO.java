package ch.bbcag.wrodit.dto.response;

import java.util.List;
import java.util.Objects;

public final class CommentPageResponseDTO extends PageableResponseDTO {
  private List<CommentResponseDTO> content;

  public List<CommentResponseDTO> getContent() {
    return content;
  }

  public void setContent(List<CommentResponseDTO> content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CommentPageResponseDTO that)) return false;
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
