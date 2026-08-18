package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entities.Post;
import ch.bbcag.wrodit.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository
        extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

    @Query("""
                SELECT COUNT(p)
                FROM Post p
                WHERE p.threads.id = :id
            """)
    long countPostsByThreadId(@Param("id") Integer id);

    @Query("""
                SELECT COUNT(DISTINCT u)
                FROM Post p
                JOIN p.users u
                WHERE p.threads.id = :id
            """)
    long countActiveUsersByThreadId(@Param("id") Integer id);

    @Query("""
                SELECT DISTINCT u
                FROM Post p
                JOIN p.users u
                WHERE p.threads.id = :id
            """)
    List<User> findActiveUsersByThreadId(
            @Param("id") Integer id,
            Pageable pageable);

    Optional<Post> findFirstByThreadsIdOrderByCreatedAtDesc(Integer id);
}
