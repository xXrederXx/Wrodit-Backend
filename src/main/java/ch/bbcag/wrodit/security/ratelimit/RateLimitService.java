package ch.bbcag.wrodit.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

  private final Map<Integer, Bucket> readBuckets = new ConcurrentHashMap<>();
  private final Map<Integer, Bucket> writeBuckets = new ConcurrentHashMap<>();

  private Bucket newReadBucket() {
    return Bucket.builder()
        .addLimit(
            Bandwidth.builder().capacity(400).refillGreedy(200, Duration.ofMinutes(1)).build())
        .build();
  }

  private Bucket newWriteBucket() {
    return Bucket.builder()
        .addLimit(Bandwidth.builder().capacity(20).refillGreedy(20, Duration.ofMinutes(1)).build())
        .build();
  }

  public Bucket getReadBucket(Integer userId) {
    return readBuckets.computeIfAbsent(userId, id -> newReadBucket());
  }

  public Bucket getWriteBucket(Integer userId) {
    return writeBuckets.computeIfAbsent(userId, id -> newWriteBucket());
  }
}
