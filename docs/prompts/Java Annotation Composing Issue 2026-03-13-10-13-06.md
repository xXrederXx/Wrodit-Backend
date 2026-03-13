# Java Annotation Composing Issue

- Exported: 13.3.2026, 11:13:06
- Source: https://chatgpt.com/c/69b3e0d8-3ea4-8325-b2f2-b2bc1fae434b
- Messages: 2

## 1. You

I read an article on how to commbine java annotations. But it dosnt work any more. Why and how do i fix it? package ch.bbcag.wrodit.util.annotation; import static java.lang.annotation.ElementType.*; import static java.lang.annotation.RetentionPolicy.*; import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Pattern; import jakarta.validation.constraints.Size; import java.lang.annotation.Retention; import java.lang.annotation.Target; @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE}) @Retention(RUNTIME) // Java annotations @NotBlank(message = "Password must not be empty") @Size(min = 8, max = 255, message = "Password length must be not more than 255") @Pattern(regexp = ".*[a-z].*", message = "Password must contain a lower case letter") @Pattern(regexp = ".*[A-Z].*", message = "Password must contain a upper case letter") @Pattern(regexp = ".*\\d.*", message = "Password must contain a number") @Pattern(regexp = ".*\\W.*", message = "Password must contain a symbol") public @interface Password {}

## 2. Assistant

Your idea is correct: **composing annotations** (creating a custom annotation that bundles several others) is a standard feature of **Jakarta Bean Validation** and used to work like that in older examples. The reason your version **doesn't work anymore** is usually one of these changes in newer stacks.

---

# 1️⃣ The real problem: repeated @Pattern

In your code you use `@Pattern` **multiple times**:

```
Java@Pattern(regexp = ".*[a-z].*", message = "Password must contain a lower case letter")@Pattern(regexp = ".*[A-Z].*", message = "Password must contain a upper case letter")@Pattern(regexp = ".*\\d.*", message = "Password must contain a number")@Pattern(regexp = ".*\\W.*", message = "Password must contain a symbol")
```

But **jakarta.validation.constraints.Pattern** is a **repeatable annotation**, which means Java internally wraps multiple annotations into a container annotation.

The container is:

```
jakarta.validation.constraints.Pattern.List
```

Some validation providers (especially newer versions of **Hibernate Validator**) require that you **explicitly use the container** when composing annotations.

---

# ✅ Fix

Wrap the patterns in `@Pattern.List`.

```
Java@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})@Retention(RUNTIME)@NotBlank(message = "Password must not be empty")@Size(min = 8, max = 255, message = "Password length must be not more than 255")@Pattern.List({    @Pattern(regexp = ".*[a-z].*", message = "Password must contain a lower case letter"),    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain a upper case letter"),    @Pattern(regexp = ".*\\d.*", message = "Password must contain a number"),    @Pattern(regexp = ".*\\W.*", message = "Password must contain a symbol")})public @interface Password {}
```

---

# 2️⃣ Recommended additions (modern Bean Validation)

Add `@Constraint` and `validatedBy = {}` to make the annotation a **proper composed constraint**.

```
Javaimport jakarta.validation.Constraint;@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})@Retention(RUNTIME)@Constraint(validatedBy = {})@NotBlank@Size(min = 8, max = 255)@Pattern.List({    @Pattern(regexp = ".*[a-z].*"),    @Pattern(regexp = ".*[A-Z].*"),    @Pattern(regexp = ".*\\d.*"),    @Pattern(regexp = ".*\\W.*")})public @interface Password {    String message() default "Invalid password";    Class<?>[] groups() default {};    Class<?>[] payload() default {};}
```

This is the **official way for composed constraints**.

---

# 3️⃣ Why older tutorials worked

Older tutorials relied on:

- **Hibernate Validator 5**
- **Bean Validation 1.1**

Those versions were more permissive about repeated annotations in composed constraints.

Newer stacks:

- **Jakarta Bean Validation 3.0**
- **Hibernate Validator 8**

are stricter.

---

# ⭐ Pro tip (better password rule)

Instead of 4 patterns, many projects use **one regex**:

```
Java@Pattern( regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).+$", message = "Password must contain upper, lower, number and symbol")
```

Cleaner and faster.

---

✅ If you want, I can also show you the **Spring Boot / Jakarta "best practice" password annotation** (with **single regex + composition + reusable messages**) — it’s about **10 lines shorter and cleaner**.
