package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entitys.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {}
