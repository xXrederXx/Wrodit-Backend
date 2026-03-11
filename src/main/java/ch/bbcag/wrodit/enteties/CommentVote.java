package ch.bbcag.wrodit.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class CommentVote {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
