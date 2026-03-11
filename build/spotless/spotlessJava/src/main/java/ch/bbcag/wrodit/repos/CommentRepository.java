package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entitys.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {}
