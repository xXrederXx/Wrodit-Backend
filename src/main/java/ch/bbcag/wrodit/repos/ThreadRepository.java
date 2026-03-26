package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entities.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ThreadRepository
    extends JpaRepository<Thread, Integer>, JpaSpecificationExecutor<Thread> {}
