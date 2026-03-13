package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class UserService {
  private final UserRepository repo;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
    this.repo = repo;
    this.passwordEncoder = passwordEncoder;
  }

  public void throwIfUnauthorized(Integer id, String password) {
    User user = repo.findById(id).orElseThrow(EntityNotFoundException::new);
    if (passwordEncoder.matches(password, user.getPasswordHash())) {
      return;
    }
    throw new AuthorizationDeniedException("Not Authorized ");
  }

  public boolean checkAuthorization(String username, String password) {
    try {
      User user = findByUsername(username);
      return passwordEncoder.matches(password, user.getPasswordHash());
    } catch (Exception e) {
      return false;
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
