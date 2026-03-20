# Injecting JWT Secret.md

- Exported: 20.03.2026,  7:58:11
- Source: https://chatgpt.com/c/69bc0bc9-39c8-8333-9a06-d1ac4ca75f21
- Messages 2

**You:**

Sadly this dosnt seem to work in combination with coolify. But i can inject env vars in the application.properties. Can we make that work?

static {
    String secretFromEnv = System.getenv("JWT_SECRET");
    if (secretFromEnv == null || secretFromEnv.length() < 32) {
      throw new RuntimeException("JWT_SECRET environment variable not set or too short");
    }
    SECRET = secretFromEnv;
  }

* * *

**ChatGPT:**

Yes — you can absolutely make this work using `application.properties`, but your current approach needs a small adjustment.

Right now you're calling:

JavaSystem.getenv("JWT_SECRET");

That only reads **OS-level environment variables**, not values from `application.properties`.

---

## ✅ Solution: Use Spring configuration instead

If you're injecting values via `application.properties`, you should let Spring handle it.

### 1. Add to `application.properties`

propertiesjwt.secret=your-super-long-secret-key-here

---

### 2. Inject it in your code

Instead of a `static` block, do this:

Java@Component  
public class JwtConfig {  
  
    @Value("${jwt.secret}")  
    private String secret;  
  
    @PostConstruct  
    public void validate() {  
        if (secret == null || secret.length() < 32) {  
            throw new RuntimeException("JWT secret too short or not set");  
        }  
    }  
  
    public String getSecret() {  
        return secret;  
    }  
}

---

### ⚠️ Why your current approach fails

* `System.getenv()` ≠ Spring properties
* `static` blocks run **before Spring initializes**
* So Spring never gets a chance to inject anything

---

## 🔁 If you REALLY want to keep static usage

You can bridge it like this:

Java@Component  
public class JwtUtil {  
  
    public static String SECRET;  
  
    @Value("${jwt.secret}")  
    private String secret;  
  
    @PostConstruct  
    public void init() {  
        if (secret == null || secret.length() < 32) {  
            throw new RuntimeException("JWT secret too short or not set");  
        }  
        SECRET = secret;  
    }  
}

---

## 🧠 Alternative (if Coolify _can_ pass env vars)

If Coolify actually supports env vars but they’re just not showing up:

Try using Spring’s built-in mapping:

propertiesjwt.secret=${JWT_SECRET}

Then your original environment variable would still work.

---

## 👍 Recommendation

Avoid `static` + `System.getenv()` in Spring apps.  
Let Spring manage configuration — it’s more flexible and works cleanly with tools like Coolify.

---

If you want, show me how you're using `SECRET` (JWT signing code), and I can help you wire it in properly.