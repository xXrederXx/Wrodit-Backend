package ch.bbcag.wrodit.security.ratelimit;

import static org.junit.jupiter.api.Assertions.*;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RateLimitServiceTest {

  private RateLimitService rateLimitService;

  @BeforeEach
  void setUp() {
    rateLimitService = new RateLimitService();
  }

  @Test
  void testGetReadBucket_returnsNonNullBucket() {
    Bucket bucket = rateLimitService.getReadBucket(1);
    assertNotNull(bucket, "Read bucket should not be null");
  }

  @Test
  void testGetWriteBucket_returnsNonNullBucket() {
    Bucket bucket = rateLimitService.getWriteBucket(1);
    assertNotNull(bucket, "Write bucket should not be null");
  }

  @Test
  void testGetReadBucket_sameUser_returnsSameBucket() {
    Bucket first = rateLimitService.getReadBucket(42);
    Bucket second = rateLimitService.getReadBucket(42);
    assertSame(first, second, "Buckets for same user should be the same instance");
  }

  @Test
  void testGetWriteBucket_sameUser_returnsSameBucket() {
    Bucket first = rateLimitService.getWriteBucket(42);
    Bucket second = rateLimitService.getWriteBucket(42);
    assertSame(first, second, "Buckets for same user should be the same instance");
  }
}
