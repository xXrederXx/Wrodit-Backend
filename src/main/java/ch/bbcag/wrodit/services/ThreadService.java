package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entitys.Thread;
import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.repos.ThreadRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

  public Page<Thread> paginatedThreads(Integer pageNumber, Integer pageSize, Sort sort) {
    return repo.findAll(PageRequest.of(pageNumber, pageSize, sort));
  }

  public Page<Thread> paginatedThreadsByUser(
      User user, Integer pageNumber, Integer pageSize, Sort sort) {
    return repo.findAllByUsersThreadUsersContaining(
        Set.of(user), PageRequest.of(pageNumber, pageSize, sort));
  }

  public Thread save(Thread thread) {
    thread.setCreatedAt(OffsetDateTime.now());
    return repo.save(thread);
  }
}
