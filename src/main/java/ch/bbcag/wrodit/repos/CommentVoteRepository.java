package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entities.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Integer> {}
