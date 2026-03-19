package ch.bbcag.wrodit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateDTO(
    @NotBlank(message = "Comment cant be blank") String content,
    Integer parentId,
    Integer postId) {}
