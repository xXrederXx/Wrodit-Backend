package ch.bbcag.wrodit.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false)
  private String passwordHash;

  @Column private String profileImagePath;

  @Column private OffsetDateTime createdAt;

  @ManyToMany
  @JoinTable(
      name = "UsersThread",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "threadId"))
  private Set<Thread> usersThreadThreads = new HashSet<>();

  @OneToMany(mappedBy = "users")
  private Set<Post> usersPosts = new HashSet<>();

  @OneToMany(mappedBy = "users")
  private Set<Comment> usersComments = new HashSet<>();

  @OneToMany(mappedBy = "users")
  private Set<CommentVote> usersCommentVotes = new HashSet<>();

  @OneToMany(mappedBy = "users")
  private Set<PostsVote> usersPostsVotes = new HashSet<>();

  public User() {}

  public User(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
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

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Set<Thread> getUsersThreadThreads() {
    return usersThreadThreads;
  }

  public void setUsersThreadThreads(final Set<Thread> usersThreadThreads) {
    this.usersThreadThreads = usersThreadThreads;
  }

  public Set<Post> getUsersPosts() {
    return usersPosts;
  }

  public void setUsersPosts(final Set<Post> usersPosts) {
    this.usersPosts = usersPosts;
  }

  public Set<Comment> getUsersComments() {
    return usersComments;
  }

  public void setUsersComments(final Set<Comment> usersComments) {
    this.usersComments = usersComments;
  }

  public Set<CommentVote> getUsersCommentVotes() {
    return usersCommentVotes;
  }

  public void setUsersCommentVotes(final Set<CommentVote> usersCommentVotes) {
    this.usersCommentVotes = usersCommentVotes;
  }

  public Set<PostsVote> getUsersPostsVotes() {
    return usersPostsVotes;
  }

  public void setUsersPostsVotes(final Set<PostsVote> usersPostsVotes) {
    this.usersPostsVotes = usersPostsVotes;
  }
}
