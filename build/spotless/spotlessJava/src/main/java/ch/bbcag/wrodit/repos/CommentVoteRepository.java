package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entitys.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Integer> {}
