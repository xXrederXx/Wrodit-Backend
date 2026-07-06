package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.response.backup.CommentBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.CommentVoteBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.PostBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.PostVoteBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.ThreadBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.UserBackupDTO;
import ch.bbcag.wrodit.dto.response.backup.UserThreadBackupDTO;
import ch.bbcag.wrodit.entities.Comment;
import ch.bbcag.wrodit.entities.CommentVote;
import ch.bbcag.wrodit.entities.Post;
import ch.bbcag.wrodit.entities.PostVote;
import ch.bbcag.wrodit.entities.Thread;
import ch.bbcag.wrodit.entities.User;

public class BackupMapper {
  private BackupMapper() {}

  public static UserBackupDTO toUserBackupDto(User user) {
    return new UserBackupDTO(
        user.getId(),
        user.getEmail(),
        user.getUsername(),
        user.getPasswordHash(),
        user.getProfileImagePath(),
        user.getCreatedAt());
  }

  public static ThreadBackupDTO toThreadBackupDto(Thread thread) {
    return new ThreadBackupDTO(
        thread.getId(),
        thread.getName(),
        thread.getDescription(),
        thread.getBannerImagePath(),
        thread.getIconImagePath(),
        thread.getCreatedAt());
  }

  public static UserThreadBackupDTO toUserThreadBackupDto(User user, Thread thread) {
    return new UserThreadBackupDTO(user.getId(), thread.getId());
  }

  public static PostBackupDTO toPostBackupDto(Post post) {
    return new PostBackupDTO(
        post.getId(),
        post.getTitle(),
        post.getContent(),
        post.getUsers() == null ? null : post.getUsers().getId(),
        post.getThreads() == null ? null : post.getThreads().getId(),
        post.getCreatedAt());
  }

  public static CommentBackupDTO toCommentBackupDto(Comment comment) {
    return new CommentBackupDTO(
        comment.getId(),
        comment.getUsers() == null ? null : comment.getUsers().getId(),
        comment.getContent(),
        comment.getCreatedAt(),
        comment.getParentComment() == null ? null : comment.getParentComment().getId(),
        comment.getPosts() == null ? null : comment.getPosts().getId());
  }

  public static PostVoteBackupDTO toPostVoteBackupDto(PostVote postVote) {
    return new PostVoteBackupDTO(
        postVote.getUsers() == null ? null : postVote.getUsers().getId(),
        postVote.getPosts() == null ? null : postVote.getPosts().getId(),
        postVote.getVote());
  }

  public static CommentVoteBackupDTO toCommentVoteBackupDto(CommentVote commentVote) {
    return new CommentVoteBackupDTO(
        commentVote.getUsers() == null ? null : commentVote.getUsers().getId(),
        commentVote.getComments() == null ? null : commentVote.getComments().getId(),
        commentVote.getVote());
  }
}
