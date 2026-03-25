package ch.bbcag.wrodit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Comment {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, columnDefinition = "longtext")
  private String content;

  @Column(nullable = false)
  private OffsetDateTime createdAt;

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
    this.id = id;
  }

  public Comment() {}

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
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
