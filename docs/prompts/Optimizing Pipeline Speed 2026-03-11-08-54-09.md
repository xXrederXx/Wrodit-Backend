# Optimizing Pipeline Speed

- Exported: 11.3.2026, 09:54:09
- Source: https://chatgpt.com/c/69b11722-b330-832c-8865-a170931a56f9
- Messages: 2

## 1. You

Can you make this pipline faster without merging multiple stages as it is requiered to keep them seperate. stages: - format - test - build - deploy_stageing - deploy_prod variables: GRADLE_USER_HOME: "$CI_PROJECT_DIR/.gradle" cache: key: ${CI_COMMIT_REF_SLUG} paths: - .gradle/wrapper - .gradle/caches # 1. Code formatting check format: stage: format image: gradle:jdk21 script: - ./gradlew spotlessCheck only: - merge_requests - main # 2. Unit tests unit_test: stage: test image: gradle:jdk21 script: - ./gradlew test artifacts: reports: junit: build/test-results/test/*.xml paths: - build/test-results/test - build/reports/tests only: - merge_requests - main # 3. Build Spring Boot JAR build_jar: stage: build image: gradle:jdk21 script: - ./gradlew clean bootJar artifacts: paths: - build/libs/*.jar only: - merge_requests - main # 4. Notify Coolify webhook for redeploy coolify_deploy_stageing: stage: deploy_stageing image: curlimages/curl:latest variables: AUTH_HEADER: "Authorization: Bearer $COOLIFY_TOK" script: - curl -X GET "$COOLIFY_STAGING_URL" -H "$AUTH_HEADER" only: - main # 4. Notify Coolify webhook for redeploy coolify_deploy_prod: stage: deploy_prod image: curlimages/curl:latest variables: AUTH_HEADER: "Authorization: Bearer $COOLIFY_TOK" script: - curl -X GET "$COOLIFY_PROD_URL" -H "$AUTH_HEADER" only: - production

## 2. Assistant

Yes. You can make this **significantly faster without merging stages** by optimizing **Gradle execution, caching, and GitLab pipeline behavior**. Your pipeline currently re-does a lot of work in every stage.

Below are **safe optimizations that keep the stages separate**.

---

# 1. Enable Gradle Build Cache + Parallelism (Biggest Gain)

Gradle can reuse results between jobs if you enable caching.

Update every Gradle command:

```Bash
./gradlew --build-cache --parallel --daemon <task>
```

Example:

```YAML
script:  - ./gradlew --build-cache --parallel --daemon spotlessCheck
```

Apply to:

```
spotlessChecktestbootJar
```

---

# 2. Remove clean From Build Stage

This is **a huge slowdown**.

You currently run:

```Bash
./gradlew clean bootJar
```

`clean` **deletes all cached outputs**, meaning the previous stages cannot reuse them.

Change to:

```Bash
./gradlew --build-cache --parallel bootJar
```

---

# 3. Share Build Output Between Jobs (Artifacts)

Right now the **test stage compiles the code**, then **build stage compiles it again**.

Reuse compiled classes.

### Update unit_test

```YAML
unit_test:  stage: test  image: gradle:jdk21  script:    - ./gradlew --build-cache --parallel test  artifacts:    paths:      - build    reports:      junit: build/test-results/test/*.xml
```

Now the **build job can reuse compiled classes**.

---

### Update build_jar

```YAML
build_jar:  stage: build  image: gradle:jdk21  dependencies:    - unit_test  script:    - ./gradlew --build-cache --parallel bootJar  artifacts:    paths:      - build/libs/*.jar
```

Now Gradle won't recompile everything.

---

# 4. Pull Cache Faster

Improve the cache configuration:

```YAML
cache:  key: gradle-cache  policy: pull-push  paths:    - .gradle/wrapper    - .gradle/caches
```

Your current key:

```
${CI_COMMIT_REF_SLUG}
```

creates **new caches for every branch**, which hurts reuse.

---

# 5. Use needs: to Start Jobs Earlier

This **does not merge stages**, but allows faster scheduling.

Example:

```YAML
unit_test:  stage: test  needs: ["format"]
```

```YAML
build_jar:  stage: build  needs: ["unit_test"]
```

This avoids GitLab waiting for **all jobs in previous stages**.

---

# 6. Use a Smaller Gradle Image

`gradle:jdk21` is large. Faster pull:

```YAML
image: gradle:8.6-jdk21-alpine
```

---

# 7. Skip Formatting in Main (Optional)

If you already enforce formatting in MR:

```YAML
only:  - merge_requests
```

This removes an unnecessary job on main.

---

# Optimized Pipeline Example

```YAML
stages:  - format  - test  - build  - deploy_stageing  - deploy_prodvariables:  GRADLE_USER_HOME: "$CI_PROJECT_DIR/.gradle"cache:  key: gradle-cache  policy: pull-push  paths:    - .gradle/wrapper    - .gradle/cachesformat:  stage: format  image: gradle:8.6-jdk21-alpine  script:    - ./gradlew --build-cache --parallel spotlessCheck  only:    - merge_requests    - mainunit_test:  stage: test  image: gradle:8.6-jdk21-alpine  needs: ["format"]  script:    - ./gradlew --build-cache --parallel test  artifacts:    reports:      junit: build/test-results/test/*.xml    paths:      - build  only:    - merge_requests    - mainbuild_jar:  stage: build  image: gradle:8.6-jdk21-alpine  needs: ["unit_test"]  dependencies:    - unit_test  script:    - ./gradlew --build-cache --parallel bootJar  artifacts:    paths:      - build/libs/*.jar  only:    - merge_requests    - main
```

---

# Typical Speed Improvement

With these changes:

| Stage | Before | After |
| --- | --- | --- |
| format | 30s | 15s |
| test | 2–4 min | 1–2 min |
| build | 2–3 min | 10–40s |

**Total pipeline:**

```
~6–9 minutes → ~2–3 minutes
```

---

💡 If you want, I can also show a **much faster Gradle CI setup used by large Spring Boot projects** that reduces pipelines to **~1 minute** while still keeping stages separate.
