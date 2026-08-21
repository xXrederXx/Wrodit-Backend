package ch.bbcag.wrodit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    indexes = {
      @Index(name = "idx_comment_post", columnList = "posts_id"),
      @Index(name = "idx_comment_parent", columnList = "parent_comments_id")
    })
public class Comment extends BaseEntity {

  @Column(nullable = false, columnDefinition = "longtext")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id", nullable = false)
  private User users;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_comments_id")
  private Comment parentComment;

  @OneToMany(mappedBy = "parentComment")
  private Set<Comment> childComments = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "posts_id")
  private Post posts;

  @OneToMany(mappedBy = "comments")
  private Set<CommentVote> commentVotes = new HashSet<>();

  public Comment(Integer id) {
    setId(id);
  }

  public Comment() {}

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public User getUsers() {
    return users;
  }

  public void setUsers(final User users) {
    this.users = users;
  }

  public Comment getParentComment() {
    return parentComment;
  }

  public void setParentComment(final Comment parentComments) {
    this.parentComment = parentComments;
  }

  public Set<Comment> getChildComments() {
    return childComments;
  }

  public void setChildComments(final Set<Comment> parentCommentsComments) {
    this.childComments = parentCommentsComments;
  }

  public Post getPosts() {
    return posts;
  }

  public void setPosts(final Post posts) {
    this.posts = posts;
  }

  public Set<CommentVote> getCommentVotes() {
    return commentVotes;
  }

  public void setCommentVotes(final Set<CommentVote> commentsCommentVotes) {
    this.commentVotes = commentsCommentVotes;
  }
}
