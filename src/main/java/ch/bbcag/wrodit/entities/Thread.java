package ch.bbcag.wrodit.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Thread extends BaseEntity {

  @Column(nullable = false, unique = true, length = 150)
  private String name;

  @Column(length = 511, name = "\"description\"")
  private String description;

  @Column private String bannerImagePath;

  @Column private String iconImagePath;

  @ManyToMany(mappedBy = "threads")
  private Set<User> users = new HashSet<>();

  @OneToMany(mappedBy = "threads")
  private Set<Post> posts = new HashSet<>();

  public Thread(Integer id) {
    setId(id);
  }

  public Thread() {}

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getBannerImagePath() {
    return bannerImagePath;
  }

  public void setBannerImagePath(final String bannerImagePath) {
    this.bannerImagePath = bannerImagePath;
  }

  public String getIconImagePath() {
    return iconImagePath;
  }

  public void setIconImagePath(final String iconImagePath) {
    this.iconImagePath = iconImagePath;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(final Set<User> usersThreadUsers) {
    this.users = usersThreadUsers;
  }

  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(final Set<Post> threadsPosts) {
    this.posts = threadsPosts;
  }
}
