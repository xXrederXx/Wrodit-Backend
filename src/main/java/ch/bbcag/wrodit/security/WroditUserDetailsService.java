package ch.bbcag.wrodit.security;

import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.repos.UserRepository;
import java.util.Collections;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WroditUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public WroditUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPasswordHash(), Collections.emptyList());
  }
}
