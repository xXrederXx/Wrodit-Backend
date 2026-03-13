package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.response.ThreadResponseDTO;
import ch.bbcag.wrodit.entitys.Thread;

public class ThreadMapper {
    public static ThreadResponseDTO toDTO(Thread therad)
    {
        return new ThreadResponseDTO(therad.getId(), therad.getName(), therad.getDescription(), therad.getCreatedAt());
    }
}
