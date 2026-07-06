package ch.bbcag.wrodit.dto.response.backup;

import java.util.List;

public record BackupResponseDTO(
    List<UserBackupDTO> users,
    List<ThreadBackupDTO> threads,
    List<UserThreadBackupDTO> usersThreads,
    List<PostBackupDTO> posts,
    List<CommentBackupDTO> comments,
    List<PostVoteBackupDTO> postVotes,
    List<CommentVoteBackupDTO> commentVotes) {}
