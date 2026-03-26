package ch.bbcag.wrodit.entities;

import jakarta.persistence.*;

@Entity
@Table(
    name = "posts_vote",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"posts_id", "users_id"})})
public class PostsVote {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Integer vote;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id", nullable = false)
  private User users;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "posts_id", nullable = false)
  private Post posts;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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

  public Post getPosts() {
    return posts;
  }

  public void setPosts(final Post posts) {
    this.posts = posts;
  }
}
