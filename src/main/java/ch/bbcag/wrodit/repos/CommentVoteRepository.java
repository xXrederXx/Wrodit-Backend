package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entities.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentVoteRepository
    extends JpaRepository<CommentVote, Integer>, JpaSpecificationExecutor<CommentVote> {}
