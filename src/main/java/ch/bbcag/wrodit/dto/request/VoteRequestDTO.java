package ch.bbcag.wrodit.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record VoteRequestDTO(
    @NotNull(message = "A vote cant be null")
        @Range(min = -1, max = 1, message = "A vote can only be 1 or -1")
        Integer vote) {}
