package ch.bbcag.wrodit.security.ratelimit;

import ch.bbcag.wrodit.security.SecurityConstants;
import ch.bbcag.wrodit.util.JwtUtil;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class RateLimitFilter extends OncePerRequestFilter {

  private final RateLimitService rateLimitService;

  public RateLimitFilter(RateLimitService rateLimitService) {
    this.rateLimitService = rateLimitService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER_NAME);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);
    Integer userId = JwtUtil.extractUserId(token);

    Bucket bucket;

    String method = request.getMethod();

    if ("GET".equalsIgnoreCase(method)) {
      bucket = rateLimitService.getReadBucket(userId);
    } else {
      bucket = rateLimitService.getWriteBucket(userId);
    }

    if (bucket.tryConsume(1)) {
      filterChain.doFilter(request, response);
    } else {
      response.setStatus(429);
      response.getWriter().write("Too many requests");
    }
  }
}
