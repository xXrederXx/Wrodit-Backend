package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entities.Post;
import ch.bbcag.wrodit.entities.Thread;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.repos.PostRepository;
import ch.bbcag.wrodit.repos.ThreadRepository;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ThreadService {
  private final ThreadRepository repo;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  public ThreadService(
      ThreadRepository repo, UserRepository userRepository, PostRepository postRepository) {
    this.repo = repo;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
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

  public Integer getTotalPosts(Integer id) {
    return Math.toIntExact(postRepository.countPostsByThreadId(id));
  }

  public Integer getTotalActiveUsers(Integer id) {
    return Math.toIntExact(postRepository.countActiveUsersByThreadId(id));
  }

  public List<User> getActiveUsers(Integer id, Integer numUsers) {
    return postRepository.findActiveUsersByThreadId(id, PageRequest.of(0, numUsers));
  }

  public Post getLastPost(Integer id) {
    return postRepository
        .findFirstByThreadsIdOrderByCreatedAtDesc(id)
        .orElseThrow(EntityNotFoundException::new);
  }
}
