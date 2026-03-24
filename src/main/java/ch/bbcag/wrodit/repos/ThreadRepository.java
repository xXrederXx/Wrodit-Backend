package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entitys.Thread;
import ch.bbcag.wrodit.entitys.User;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ThreadRepository extends JpaRepository<Thread, Integer>, JpaSpecificationExecutor<Thread> {
}
