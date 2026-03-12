package ch.bbcag.wrodit.services;

import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.NotImplementedException;
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

  public void throwIfUnauthorized(Integer id, String password) {
    User user = repo.findById(id).orElseThrow(EntityNotFoundException::new);
    if (passwordEncoder.matches(password, user.getPasswordHash())) {
      return;
    }
    throw new AuthorizationDeniedException("Not Authorized ");
  }

  public User findById(int id) {
    return repo.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public User findByUsername(String username)
  {
    throw new NotImplementedException("");
  }

  public User insert(User user) {
    user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
    return repo.save(user);
  }

  public void deleteById(int id) {
    repo.deleteById(id);
  }
}
