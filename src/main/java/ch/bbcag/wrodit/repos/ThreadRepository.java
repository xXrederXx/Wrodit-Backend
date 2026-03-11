package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entitys.Thread;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ThreadRepository extends JpaRepository<Thread, Integer> {
}
