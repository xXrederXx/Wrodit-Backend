package ch.bbcag.wrodit;

import ch.bbcag.wrodit.entities.Comment;
import ch.bbcag.wrodit.entities.Post;
import ch.bbcag.wrodit.entities.Thread;
import ch.bbcag.wrodit.entities.User;
import java.time.ZoneOffset;

public class TestingUtil {
  public static final String CONTENT_TYPE_JSON = "application/json";
  public static final long MAX_TIME_CHECK_DIFF =
      5; // This is the maximum amount off error if checking the automatic time generation in
  // seconds
  public static final ZoneOffset TIME_CHECK_OFFSET = ZoneOffset.UTC;

  public static Comment[] generateComments(int n) {
    Comment[] comments = new Comment[n];
    for (int i = 0; i < n; i++) {
      var comment = new Comment();
      comment.setId(i);
      comment.setContent("MOCK CONTENT " + i);
      comment.setPosts(new Post(i));
      comment.setUsers(new User(i));
      comments[i] = comment;
    }
    return comments;
  }

  public static Post[] generatePosts(int n) {
    Post[] posts = new Post[n];
    for (int i = 0; i < n; i++) {
      var post = new Post();
      post.setId(i);
      post.setUsers(new User(1));
      post.setContent("MOCK CONTENT " + i);
      post.setTitle("MOCK TITLE " + i);
      post.setThreads(new Thread(1));
      posts[i] = post;
    }
    return posts;
  }

  public static Thread[] generateThreads(int n) {
    Thread[] threads = new Thread[n];
    for (int i = 0; i < n; i++) {
      var thread = new Thread();
      thread.setId(i);
      thread.setName("Tester" + i);
      thread.setDescription(i + "test@test.com");
      threads[i] = thread;
    }
    return threads;
  }

  public static User generateUser() {
    User mockUser = new User();
    mockUser.setId(1);
    mockUser.setUsername("Tester");
    mockUser.setEmail("test@test.com");
    mockUser.setPasswordHash("Some-Long-Hash");
    return mockUser;
  }
}
