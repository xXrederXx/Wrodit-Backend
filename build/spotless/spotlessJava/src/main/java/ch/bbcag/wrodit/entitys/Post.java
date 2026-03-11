package ch.bbcag.wrodit.entitys;

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
public class Post {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "longtext")
  private String content;

  @Column(nullable = false)
  private OffsetDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id", nullable = false)
  private User users;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "threads_id", nullable = false)
  private Thread threads;

  @OneToMany(mappedBy = "posts")
  private Set<Comment> postsComments = new HashSet<>();

  @OneToMany(mappedBy = "posts")
  private Set<PostsVote> postsPostsVotes = new HashSet<>();

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
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

  public Thread getThreads() {
    return threads;
  }

  public void setThreads(final Thread threads) {
    this.threads = threads;
  }

  public Set<Comment> getPostsComments() {
    return postsComments;
  }

  public void setPostsComments(final Set<Comment> postsComments) {
    this.postsComments = postsComments;
  }

  public Set<PostsVote> getPostsPostsVotes() {
    return postsPostsVotes;
  }

  public void setPostsPostsVotes(final Set<PostsVote> postsPostsVotes) {
    this.postsPostsVotes = postsPostsVotes;
  }
}
