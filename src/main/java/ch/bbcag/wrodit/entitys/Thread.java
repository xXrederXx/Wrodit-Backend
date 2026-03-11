package ch.bbcag.wrodit.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Thread {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(length = 511, name = "\"description\"")
    private String description;

    @Column
    private String bannerImagePath;

    @Column
    private String iconImagePath;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(mappedBy = "usersThreadThreads")
    private Set<User> usersThreadUsers = new HashSet<>();

    @OneToMany(mappedBy = "threads")
    private Set<Post> threadsPosts = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<User> getUsersThreadUsers() {
        return usersThreadUsers;
    }

    public void setUsersThreadUsers(final Set<User> usersThreadUsers) {
        this.usersThreadUsers = usersThreadUsers;
    }

    public Set<Post> getThreadsPosts() {
        return threadsPosts;
    }

    public void setThreadsPosts(final Set<Post> threadsPosts) {
        this.threadsPosts = threadsPosts;
    }

}
