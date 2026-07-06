package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.dto.response.backup.BackupResponseDTO;
import ch.bbcag.wrodit.dto.response.backup.CommentBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.CommentVoteBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.PostBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.PostVoteBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.ThreadBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.UserBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.UserThreadBackupDTO;
import ch.bbcag.wrodit.mapper.BackupMapper;
import ch.bbcag.wrodit.repos.CommentRepository;
import ch.bbcag.wrodit.repos.CommentVoteRepository;
import ch.bbcag.wrodit.repos.PostRepository;
import ch.bbcag.wrodit.repos.PostVoteRepository;
import ch.bbcag.wrodit.repos.ThreadRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BackupService {
  private final UserRepository userRepository;
  private final ThreadRepository threadRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final PostVoteRepository postVoteRepository;
  private final CommentVoteRepository commentVoteRepository;

  public BackupService(
      UserRepository userRepository,
      ThreadRepository threadRepository,
      PostRepository postRepository,
      CommentRepository commentRepository,
      PostVoteRepository postVoteRepository,
      CommentVoteRepository commentVoteRepository) {
    this.userRepository = userRepository;
    this.threadRepository = threadRepository;
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.postVoteRepository = postVoteRepository;
    this.commentVoteRepository = commentVoteRepository;
  }

  public BackupResponseDTO getBackup() {
    List<UserBackupDTO> users =
        userRepository.findAll().stream().map(BackupMapper::toUserBackupDto).toList();
    List<ThreadBackupDTO> threads =
        threadRepository.findAll().stream().map(BackupMapper::toThreadBackupDto).toList();
    List<UserThreadBackupDTO> usersThreads =
        userRepository.findAll().stream()
            .flatMap(
                user ->
                    user.getThreads().stream()
                        .map(thread -> BackupMapper.toUserThreadBackupDto(user, thread)))
            .toList();
    List<PostBackupDTO> posts =
        postRepository.findAll().stream().map(BackupMapper::toPostBackupDto).toList();
    List<CommentBackupDTO> comments =
        commentRepository.findAll().stream().map(BackupMapper::toCommentBackupDto).toList();
    List<PostVoteBackupDTO> postVotes =
        postVoteRepository.findAll().stream().map(BackupMapper::toPostVoteBackupDto).toList();
    List<CommentVoteBackupDTO> commentVotes =
        commentVoteRepository.findAll().stream().map(BackupMapper::toCommentVoteBackupDto).toList();

    return new BackupResponseDTO(
        users, threads, usersThreads, posts, comments, postVotes, commentVotes);
  }
}
