package ch.bbcag.wrodit.util;

import static ch.bbcag.wrodit.TestingConstants.MAX_TIME_CHECK_DIFF;
import static ch.bbcag.wrodit.TestingConstants.TIME_CHECK_OFFSET;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

class ErrorResponseBuilderTest {

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
            < MAX_TIME_CHECK_DIFF,
        "Timestamp should be automatically set to current time");
  }
}
