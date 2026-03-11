package ch.bbcag.wrodit.repos;

import ch.bbcag.wrodit.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}
