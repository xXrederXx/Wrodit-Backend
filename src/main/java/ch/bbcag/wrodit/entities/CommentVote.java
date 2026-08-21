package ch.bbcag.wrodit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "comment_vote",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"comments_id", "users_id"})})
public class CommentVote extends BaseEntity {

  @Column(nullable = false)
  private Integer vote;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id", nullable = false)
  private User users;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comments_id", nullable = false)
  private Comment comments;

  public Integer getVote() {
    return vote;
  }

  public void setVote(final Integer vote) {
    this.vote = vote;
  }

  public User getUsers() {
    return users;
  }

  public void setUsers(final User users) {
    this.users = users;
  }

  public Comment getComments() {
    return comments;
  }

  public void setComments(final Comment comments) {
    this.comments = comments;
  }
}
