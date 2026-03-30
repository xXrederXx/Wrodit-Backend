## Spring Boot PATCH Validation

- Exported: 30.3.2026, 09:27:47
- Source: https://chatgpt.com/c/69ca2232-6d68-8389-bdb4-618b2e590ef8
- Messages: 2

**You:**

So in my spring boot project i need to validate request bodys for post methods. But i cant do this for Patches because then it would need to send the whole data again. But i still want that only existing fields with the right type are accepted. So if the dto allows a string named x with size max = 32 it should only check if a field is named x and is a string and not if it is in length. Ontop if the dto only has fields x, y, z and the requests sends A then it should fail too.

* * *

**ChatGPT:**

What you’re running into is a classic difference between **full validation (POST/PUT)** and **partial updates (PATCH)** in Spring Boot.

You basically want:

1. Only **known fields** allowed (reject unknown ones)
2. Only **correct types** allowed
3. But **skip strict validations** like `@Size`, `@NotNull`, etc. for PATCH

That’s totally doable — here are the clean approaches 👇

---

# ✅ 1. Reject unknown fields (VERY important for PATCH)

Configure Jackson (the JSON parser used by Spring):

Java@Bean  
public ObjectMapper objectMapper() {  
    return JsonMapper.builder()  
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)  
        .build();  
}

👉 This ensures:

* `{ "A": "test" }` ❌ → fails if `A` is not in DTO
* Only `x, y, z` are allowed

---

# ✅ 2. Allow partial fields but still validate types

Spring already does this automatically when binding to a DTO:

Java@PatchMapping("/{id}")  
public ResponseEntity<?> update(  
    @PathVariable Long id,  
    @RequestBody MyPatchDto dto  
) {  
    ...  
}

If the client sends:

JSON{ "x": 123 }

and `x` is `String` → ❌ fails automatically (type mismatch)

So ✔ type validation is already handled.

---

# ✅ 3. Skip strict validations (like @Size) for PATCH

This is the key part.

Use **Validation Groups**:

Javapublic interface CreateValidation {}  
public interface PatchValidation {}

---

### DTO example

Javapublic class MyDto {  
  
    @NotNull(groups = CreateValidation.class)  
    @Size(max = 32, groups = CreateValidation.class)  
    private String x;  
  
    private Integer y;  
}

👉 Notice:

* No validation applied for `PatchValidation`
* So PATCH won’t enforce `@Size`, `@NotNull`

---

### Controller usage

#### POST (strict validation)

Java@PostMapping  
public ResponseEntity<?> create(  
    @Validated(CreateValidation.class)  
    @RequestBody MyDto dto  
) {  
    ...  
}

#### PATCH (relaxed validation)

Java@PatchMapping("/{id}")  
public ResponseEntity<?> patch(  
    @Validated(PatchValidation.class)  
    @RequestBody MyDto dto  
) {  
    ...  
}

---

# ✅ 4. Optional: Only validate provided fields (advanced)

If you _do_ want validation like `@Size` **only when field is present**, you can:

### Option A — Use `Optional<T>`

Javaprivate Optional<@Size(max = 32) String> x;

Then:

* Missing field → ignored ✅
* Present but too long → ❌

---

### Option B — Custom validator (more control)

You can write a custom annotation like:

Java@ValidateIfPresent  
@Size(max = 32)  
private String x;

---

# ⚠️ Important design note

PATCH semantics usually follow:

* **Only validate fields that are present**
* Not validating missing fields is correct behavior

---

# 🧠 Clean architecture approach (recommended)

Most real-world projects do:

* `CreateDto` → strict validation
* `UpdateDto` → partial fields + relaxed validation

Example:

Javaclass CreateUserDto {  
    @NotNull  
    @Size(max = 32)  
    String name;  
}  
  
class PatchUserDto {  
    String name; // no @NotNull, maybe optional validation  
}

---

# ✅ Final result (what you want)

| Case | Result |
| --- | --- |
| Unknown field (`A`) | ❌ rejected |
| Wrong type (`x: 123`) | ❌ rejected |
| Missing field | ✅ allowed |
| Too long string (PATCH) | ✅ allowed (if using groups) |

---

If you want, I can show you a **clean Map-based PATCH approach** (like `Map<String, Object>` + manual validation), which is even stricter and sometimes used in production APIs.