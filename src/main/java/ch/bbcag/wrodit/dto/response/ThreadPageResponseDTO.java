package ch.bbcag.wrodit.dto.response;

import java.util.List;
import java.util.Objects;

public final class ThreadPageResponseDTO extends PageableResponseDTO {
  private List<ThreadResponseDTO> content;

  public List<ThreadResponseDTO> getContent() {
    return content;
  }

  public void setContent(List<ThreadResponseDTO> content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ThreadPageResponseDTO that)) return false;
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
