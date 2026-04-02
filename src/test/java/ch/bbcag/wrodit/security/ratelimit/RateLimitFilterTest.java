package ch.bbcag.wrodit.security.ratelimit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ch.bbcag.wrodit.util.JwtUtil;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RateLimitFilterTest {

  private RateLimitService mockRateLimitService;
  private RateLimitFilter filter;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private FilterChain mockChain;

  @BeforeEach
  void setUp() {
    mockRateLimitService = mock(RateLimitService.class);
    filter = new RateLimitFilter(mockRateLimitService);
    mockRequest = mock(HttpServletRequest.class);
    mockResponse = mock(HttpServletResponse.class);
    mockChain = mock(FilterChain.class);
  }

  @Test
  void testDoFilterInternal_allowsRequestWhenBucketHasTokens() throws Exception {
    Bucket bucket = mock(Bucket.class);
    when(bucket.tryConsume(1)).thenReturn(true);

    when(mockRequest.getHeader("Authorization")).thenReturn("Bearer valid-token");
    when(mockRequest.getMethod()).thenReturn("GET");
    when(mockRateLimitService.getReadBucket(anyInt())).thenReturn(bucket);

    // Mock JWT util
    try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
      jwtUtilMock.when(() -> JwtUtil.extractUserId("valid-token")).thenReturn(123);

      filter.doFilterInternal(mockRequest, mockResponse, mockChain);

      verify(mockChain, times(1)).doFilter(mockRequest, mockResponse);
      verify(mockResponse, never()).setStatus(429);
    }
  }

  @Test
  void testDoFilterInternal_blocksRequestWhenBucketEmpty() throws Exception {
    Bucket bucket = mock(Bucket.class);
    when(bucket.tryConsume(1)).thenReturn(false);

    when(mockRequest.getHeader("Authorization")).thenReturn("Bearer valid-token");
    when(mockRequest.getMethod()).thenReturn("POST");
    when(mockRateLimitService.getWriteBucket(anyInt())).thenReturn(bucket);

    StringWriter writer = new StringWriter();
    when(mockResponse.getWriter()).thenReturn(new PrintWriter(writer));

    try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
      jwtUtilMock.when(() -> JwtUtil.extractUserId("valid-token")).thenReturn(123);

      filter.doFilterInternal(mockRequest, mockResponse, mockChain);

      verify(mockChain, never()).doFilter(mockRequest, mockResponse);
      verify(mockResponse).setStatus(429);
      assertTrue(writer.toString().contains("Too many requests"));
    }
  }

  @Test
  void testDoFilterInternal_skipsRateLimitWithoutAuthHeader() throws Exception {
    when(mockRequest.getHeader("Authorization")).thenReturn(null);

    filter.doFilterInternal(mockRequest, mockResponse, mockChain);

    verify(mockChain, times(1)).doFilter(mockRequest, mockResponse);
    verifyNoInteractions(mockRateLimitService);
  }
}
