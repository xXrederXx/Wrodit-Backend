package ch.bbcag.wrodit;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

@SetEnvironmentVariable(key = "JWT_SECRET", value = "Test-JWT-Secret-ONLY-4-TESTING-PURPOSE")
@SpringBootTest(properties = "spring.config.name=application-test")
class TestBackApplicationTests {

  @Test
  void contextLoads() {}
}
