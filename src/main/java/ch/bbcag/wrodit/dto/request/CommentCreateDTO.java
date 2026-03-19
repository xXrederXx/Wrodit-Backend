package ch.bbcag.wrodit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateDTO(@NotBlank(message = "Comment cant be blank") String content, Integer parentId, Integer postId) {
}
