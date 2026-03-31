package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entities.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostVoteRepository
    extends JpaRepository<PostVote, Integer>, JpaSpecificationExecutor<PostVote> {}
