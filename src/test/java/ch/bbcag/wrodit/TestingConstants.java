package ch.bbcag.wrodit;

import java.time.ZoneOffset;

public class TestingConstants {
  public static final String CONTENT_TYPE_JSON = "application/json";
  public static final long MAX_TIME_CHECK_DIFF =
      5; // This is the maximum amount off error if checking the automatic time generation in
  // seconds
  public static final ZoneOffset TIME_CHECK_OFFSET = ZoneOffset.UTC;
}
