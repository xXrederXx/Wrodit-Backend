package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entities.PostsVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostsVoteRepository
    extends JpaRepository<PostsVote, Integer>, JpaSpecificationExecutor<PostsVote> {}
