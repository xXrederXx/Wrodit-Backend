package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository repo;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
    this.repo = repo;
    this.passwordEncoder = passwordEncoder;
  }

  public User throwIfUnauthorized(Integer id, String password) {
    User user = findById(id);
    if (passwordEncoder.matches(password, user.getPasswordHash())) {
      return user;
    }
    throw new AuthorizationDeniedException("Not Authorized");
  }

  public Optional<User> checkAuthorization(String username, String password) {
    try {
      User user = findByUsername(username);
      return passwordEncoder.matches(password, user.getPasswordHash())
          ? Optional.of(user)
          : Optional.empty();
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public User findById(int id) {
    return repo.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public User findByUsername(String username) {
    return repo.findByUsername(username).orElseThrow(EntityNotFoundException::new);
  }

  public User insert(User user) {
    user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
    user.setCreatedAt(OffsetDateTime.now());
    return repo.save(user);
  }

  public void deleteById(int id) {
    repo.deleteById(id);
  }
}
