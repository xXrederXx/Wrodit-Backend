package ch.bbcag.wrodit.dto.response;

import java.util.Objects;

public class PageableResponseDTO {
  private Integer page;
  private Integer size;
  private Long totalElements;
  private Integer totalPages;
  private Boolean first;
  private Boolean last;

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(Long totalElements) {
    this.totalElements = totalElements;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public Boolean getFirst() {
    return first;
  }

  public void setFirst(Boolean first) {
    this.first = first;
  }

  public Boolean getLast() {
    return last;
  }

  public void setLast(Boolean last) {
    this.last = last;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PageableResponseDTO that)) return false;
    return Objects.equals(page, that.page)
        && Objects.equals(size, that.size)
        && Objects.equals(totalElements, that.totalElements)
        && Objects.equals(totalPages, that.totalPages)
        && Objects.equals(first, that.first)
        && Objects.equals(last, that.last);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, size, totalElements, totalPages, first, last);
  }

  @Override
  public String toString() {
    return "PageableResponseDTO{"
        + "page="
        + page
        + ", size="
        + size
        + ", totalElements="
        + totalElements
        + ", totalPages="
        + totalPages
        + ", first="
        + first
        + ", last="
        + last
        + '}';
  }
}
