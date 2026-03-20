package ch.bbcag.wrodit.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

class ErrorResponseBuilderTest {
  static final long TIME_CHECK_DIFF = 5;
  static final ZoneOffset TIME_CHECK_OFFSET = ZoneOffset.UTC;

  @Test
  void checkResponse_whenBuildingErrorCorrectly_thenCorrectResponse() {
    String testBody = "TEST";
    LocalDateTime testTime = LocalDateTime.now();
    HttpStatus testStatus = HttpStatus.CONFLICT;
    var response =
        ErrorResponseBuilder.create()
            .withStatus(testStatus)
            .withBody(testBody)
            .withTimestamp(testTime)
            .buildResponse();

    Assert.isTrue(response.getStatusCode() == testStatus, "Status code should be set");
    Assert.notNull(response.getBody(), "Response should have a body");
    Assert.isTrue(
        response.getBody().timestamp() == testTime, "Set Timestamp should not be overridden");
    Assert.isTrue(response.getBody().body() == testBody, "Response Body should be correctly set");
  }

  @Test
  void checkResponse_whenBuildingErrorNoTime_thenCorrectTime() {
    var response =
        ErrorResponseBuilder.create()
            .withStatus(HttpStatus.CONFLICT)
            .withBody("TEST")
            .buildResponse();
    Assert.notNull(response.getBody(), "Response should have a body");
    Assert.isTrue(
        LocalDateTime.now().toEpochSecond(TIME_CHECK_OFFSET)
                - response.getBody().timestamp().toEpochSecond(TIME_CHECK_OFFSET)
            < TIME_CHECK_DIFF,
        "Timestamp should be automatically set to current time");
  }
}
