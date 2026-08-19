package ch.bbcag.wrodit.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ch.bbcag.wrodit.entities.Comment;

public interface CommentRepository
        extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
    @Query(value = """
            WITH RECURSIVE comment_tree AS (
                SELECT id
                FROM comment
                WHERE posts_id = :postId

                UNION ALL

                SELECT c.id
                FROM comment c
                INNER JOIN comment_tree ct
                    ON c.parent_comments_id = ct.id
            )
            SELECT COUNT(*)
            FROM comment_tree
            """, nativeQuery = true)
    long countCommentsByPostId(@Param("postId") Integer postId);
}
