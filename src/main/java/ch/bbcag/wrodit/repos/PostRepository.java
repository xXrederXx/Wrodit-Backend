package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository
    extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {}
