package ch.bbcag.wrodit.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class User extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false)
  private String passwordHash;

  @Column private String profileImagePath;

  @ManyToMany
  @JoinTable(
      name = "UsersThread",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "threadId"))
  private Set<Thread> threads = new HashSet<>();

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<Post> posts = new HashSet<>();

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<Comment> comments = new HashSet<>();

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<CommentVote> commentVotes = new HashSet<>();

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<PostVote> postVotes = new HashSet<>();

  public User() {}

  public User(Integer id) {
    setId(id);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(final String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getProfileImagePath() {
    return profileImagePath;
  }

  public void setProfileImagePath(final String profileImagePath) {
    this.profileImagePath = profileImagePath;
  }

  public Set<Thread> getThreads() {
    return threads;
  }

  public void setThreads(final Set<Thread> usersThreadThreads) {
    this.threads = usersThreadThreads;
  }

  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(final Set<Post> usersPosts) {
    this.posts = usersPosts;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(final Set<Comment> usersComments) {
    this.comments = usersComments;
  }

  public Set<CommentVote> getCommentVotes() {
    return commentVotes;
  }

  public void setCommentVotes(final Set<CommentVote> usersCommentVotes) {
    this.commentVotes = usersCommentVotes;
  }

  public Set<PostVote> getPostVotes() {
    return postVotes;
  }

  public void setPostVotes(final Set<PostVote> usersPostVotes) {
    this.postVotes = usersPostVotes;
  }
}
