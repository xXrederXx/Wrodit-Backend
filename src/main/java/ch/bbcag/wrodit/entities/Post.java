package ch.bbcag.wrodit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Post extends BaseEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "longtext")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id", nullable = false)
  private User users;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "threads_id", nullable = false)
  private Thread threads;

  @OneToMany(mappedBy = "posts")
  private Set<Comment> comments = new HashSet<>();

  @OneToMany(mappedBy = "posts")
  private Set<PostVote> postVotes = new HashSet<>();

  public Post(Integer id) {
    setId(id);
  }

  public Post() {}

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

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(final Set<Comment> postsComments) {
    this.comments = postsComments;
  }

  public Set<PostVote> getPostVotes() {
    return postVotes;
  }

  public void setPostVotes(final Set<PostVote> postsPostVotes) {
    this.postVotes = postsPostVotes;
  }
}
