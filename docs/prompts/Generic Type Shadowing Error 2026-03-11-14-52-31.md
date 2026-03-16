# Generic Type Shadowing Error

- Exported: 11.3.2026, 15:52:31
- Source: https://chatgpt.com/c/69b18123-98ac-832f-8c07-3a9f5fc6215b
- Messages: 2

## 1. You

Why do i get an error in the withBody function about the generic types??? package ch.bbcag.wrodit.util; import org.springframework.http.HttpStatus; import java.time.LocalDateTime; public class ErrorResponseBuilder<TBody> { private LocalDateTime timestamp; private TBody body; private HttpStatus status; public static <TBody> ErrorResponseBuilder<TBody> create() { return new ErrorResponseBuilder<>(); } public <TBody> ErrorResponseBuilder<TBody> withBody(TBody body) { this.body = body; return this; } }

## 2. Assistant

Ah! I see exactly what’s causing your error. The issue is **shadowing of the generic type `TBody`** in your `withBody` method. Let me explain carefully.

---

### The code you have:

```Java
public class ErrorResponseBuilder<TBody> {    private TBody body;    public static <TBody> ErrorResponseBuilder<TBody> create() {        return new ErrorResponseBuilder<>();    }    public <TBody> ErrorResponseBuilder<TBody> withBody(TBody body) {        this.body = body; // ERROR HERE        return this;    }}
```

---

### What’s wrong

1. The class itself **already has a generic type `TBody`**: So `this.body` is of type `TBody`.
```Java
public class ErrorResponseBuilder<TBody> { ... }
```
2. But in your `withBody` method, you **redeclare the generic type** `TBody` locally: This **shadows** the class-level `TBody`. Now, inside the method: The compiler sees them as potentially **different types**, so `this.body = body` gives an error.
```Java
public <TBody> ErrorResponseBuilder<TBody> withBody(TBody body)
```
- `this.body` is the class-level `TBody`.
- `body` parameter is the method-level `TBody`.

---

### How to fix it

You just need to **use the class-level generic type**, don’t redeclare it in the method:

```Java
public ErrorResponseBuilder<TBody> withBody(TBody body) {    this.body = body;    return this;}
```

---

### Fixed class

```Java
package ch.bbcag.wrodit.util;import org.springframework.http.HttpStatus;import java.time.LocalDateTime;public class ErrorResponseBuilder<TBody> {    private LocalDateTime timestamp;    private TBody body;    private HttpStatus status;    public static <TBody> ErrorResponseBuilder<TBody> create() {        return new ErrorResponseBuilder<>();    }    public ErrorResponseBuilder<TBody> withBody(TBody body) {        this.body = body;        return this;    }    // You can add more builder methods, e.g., withStatus, withTimestamp    public ErrorResponseBuilder<TBody> withStatus(HttpStatus status) {        this.status = status;        return this;    }    public ErrorResponseBuilder<TBody> withTimestamp(LocalDateTime timestamp) {        this.timestamp = timestamp;        return this;    }}
```

✅ Now `withBody` correctly uses the **class-level generic**, and the compiler is happy.

---

If you want, I can also show a **cleaner version** of this builder that avoids even having to specify the generic when calling `create()`—so it feels more like a fluent API.

Do you want me to do that?
