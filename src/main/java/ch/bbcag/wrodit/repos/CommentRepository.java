package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository
    extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {}
