package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entitys.Thread;
import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.repos.ThreadRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ThreadService {
  private final ThreadRepository repo;

  public ThreadService(ThreadRepository repo) {
    this.repo = repo;
  }

  public Thread findById(Integer id) {
    return repo.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public Page<Thread> paginatedThreads(Pageable pageable) {
    return repo.findAll(pageable);
  }

  public Page<Thread> paginatedThreadsByUser(User user, Pageable page) {
    return repo.findAllByUsersThreadUsersContaining(Set.of(user), page);
  }
}
