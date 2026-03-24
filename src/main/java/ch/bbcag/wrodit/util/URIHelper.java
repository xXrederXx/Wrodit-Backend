package ch.bbcag.wrodit.util;

public class URIHelper {
  private URIHelper() {
    // hide ctor
  }

  public static String join(String... parts) {
    StringBuilder builder = new StringBuilder();
    for (String part : parts) {
      builder.append(part);
      builder.append('/');
    }
    builder.deleteCharAt(builder.length() - 1);
    return builder.toString();
  }
}
