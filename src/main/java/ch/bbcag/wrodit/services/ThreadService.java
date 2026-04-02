package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entities.Thread;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.repos.ThreadRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ThreadService {
  private final ThreadRepository repo;
  private final UserRepository userRepository;

  public ThreadService(ThreadRepository repo, UserRepository userRepository) {
    this.repo = repo;
    this.userRepository = userRepository;
  }

  public Thread findById(Integer id) {
    return repo.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public Page<Thread> paginatedThreads(Pageable pageable) {
    return repo.findAll(pageable);
  }

  public Page<Thread> paginatedThreadsByUser(Integer userId, Pageable page) {
    return repo.findAll(buildSpecification(userId), page);
  }

  public Thread save(Thread thread, Integer userId) {
    User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

    thread.setCreatedAt(OffsetDateTime.now());

    repo.save(thread);

    user.getThreads().add(thread);
    userRepository.save(user);

    return thread;
  }

  private Specification<Thread> buildSpecification(Integer userId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (userId != null) {
        predicates.add(criteriaBuilder.equal(root.get("users").get("id"), userId));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
