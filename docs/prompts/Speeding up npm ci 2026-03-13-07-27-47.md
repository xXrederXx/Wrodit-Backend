# Speeding up npm ci

- Exported: 13.3.2026, 08:27:47
- Source: https://chatgpt.com/c/69b3b96f-6c44-8396-922d-90215d01ff23
- Messages: 4

## 1. You

What do you think about this? can we make it faster? Do we even need npm ci and build

## 2. Assistant

I’m missing the code/config you’re referring to. Could you paste the snippet or the CI step (e.g., your GitHub Actions / GitLab CI / Dockerfile) you mean? Then I can suggest concrete speedups. 🚀

In general though, regarding **`npm ci` and `

## 3. You

image: node:20 stages: - install - lint - build - coolify_deploy_staging - coolify_deploy_prod cache: paths: - node_modules/ install: stage: install script: - npm ci lint: stage: lint script: - npm run lint build: stage: build script: - npm run buildStage artifacts: paths: - dist # bei Vite # - build # bei Create React App # 4. Notify Coolify webhook for redeploy coolify_deploy_staging: stage: coolify_deploy_staging image: curlimages/curl:latest variables: AUTH_HEADER: "Authorization: Bearer $COOLIFY_TOK" script: - curl -X GET "$COOLIFY_STAGING_URL" -H "$AUTH_HEADER" only: - main # 4. Notify Coolify webhook for redeploy coolify_deploy_prod: stage: coolify_deploy_prod image: curlimages/curl:latest variables: AUTH_HEADER: "Authorization: Bearer $COOLIFY_TOK" script: - curl -X GET "$COOLIFY_PRODUCTION_URL" -H "$AUTH_HEADER" only: - production

## 4. Assistant

Your pipeline works, but there are a few things that are **slowing it down unnecessarily**. Let’s go through the important parts.

---

# 1️⃣ The biggest issue: node_modules cache + npm ci

Right now you have:

```
YAMLcache:  paths:    - node_modules/
```

and:

```
YAMLinstall:  script:    - npm ci
```

This combination is **not ideal**.

Why:

- `npm ci` **deletes `node_modules` every time**
- so the cache is **basically ignored**

👉 Result: you pay the cost of caching **and** reinstalling.

### Better approach

Cache the **npm cache**, not `node_modules`.

```
YAMLcache:  paths:    - .npm/
```

and install with:

```
YAMLnpm ci --cache .npm --prefer-offline
```

This is **much faster**.

---

# 2️⃣ Jobs don't share installed dependencies

Right now:

```
installlintbuild
```

Each job runs in a **fresh container**.

That means:

- `install` installs dependencies
- `lint` has **no dependencies**
- `build` has **no dependencies**

Unless your runner restores `node_modules` cache perfectly, these will break or reinstall.

Better: use **artifacts** from install.

Example:

```
YAMLinstall:  stage: install  script:    - npm ci --cache .npm --prefer-offline  artifacts:    paths:      - node_modules
```

Then:

```
YAMLlint:  stage: lint  dependencies:    - install  script:    - npm run lint
```

and:

```
YAMLbuild:  stage: build  dependencies:    - install  script:    - npm run buildStage
```

This avoids reinstalling.

---

# 3️⃣ Do you even need the build stage?

That depends on **Coolify**.

Two possibilities:

### Case A — Coolify builds the project

Then **your build stage is pointless**.

You can remove:

```
installbuild
```

and keep only:

```
lint → deploy
```

---

### Case B — GitLab builds and Coolify deploys artifacts

Then the **build stage is correct**, and Coolify should deploy the `dist` folder.

---

# 4️⃣ Your pipeline can be much simpler

If Coolify builds everything, the **fastest pipeline** would be:

```
YAMLimage: node:20stages:  - lint  - deploy_staging  - deploy_prodcache:  paths:    - .npm/lint:  stage: lint  script:    - npm ci --cache .npm --prefer-offline    - npm run lintcoolify_deploy_staging:  stage: deploy_staging  image: curlimages/curl:latest  script:    - curl -X GET "$COOLIFY_STAGING_URL" -H "Authorization: Bearer $COOLIFY_TOK"  only:    - maincoolify_deploy_prod:  stage: deploy_prod  image: curlimages/curl:latest  script:    - curl -X GET "$COOLIFY_PRODUCTION_URL" -H "Authorization: Bearer $COOLIFY_TOK"  only:    - production
```

That pipeline runs in **~20–30 seconds**.

---

# 5️⃣ Another big speed improvement (optional)

Add:

```
YAMLdefault:  interruptible: true
```

Then if you push multiple commits quickly:

- old pipelines **cancel automatically**

Huge CI speed improvement.

---

# ⭐ My recommendation for your case

Since you use **Coolify**, the **cleanest architecture** is:

GitLab CI only:

```
linttrigger deploy
```

Coolify:

```
buildrun container
```

This keeps CI **very fast** and avoids double builds.

---

💡 If you want, I can also show you a **super-optimized GitLab + Coolify pipeline (≈5-10s runtime)** that most startups use.
