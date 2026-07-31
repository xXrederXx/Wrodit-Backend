# Wrodit

**Main**

[![Pipeline status](https://github.com/xXrederXx/Wrodit-Backend/actions/workflows/cicd.yml/badge.svg?branch=main)](https://github.com/xXrederXx/Wrodit-Backend/actions/workflows/cicd.yml?query=branch%3Amain)

[![Coverage report](https://img.shields.io/badge/coverage-JaCoCo-blue)](https://github.com/xXrederXx/Wrodit-Backend/actions/workflows/cicd.yml?query=branch%3Amain)

**Production**

[![Pipeline status](https://github.com/xXrederXx/Wrodit-Backend/actions/workflows/cicd.yml/badge.svg?branch=production)](https://github.com/xXrederXx/Wrodit-Backend/actions/workflows/cicd.yml?query=branch%3Aproduction)

[![Coverage report](https://img.shields.io/badge/coverage-JaCoCo-blue)](https://github.com/xXrederXx/Wrodit-Backend/actions/workflows/cicd.yml?query=branch%3Aproduction)

## Dokumentation

[Dokumentation](./docs)

## Lokal ausführen

Projekt klonen

```bash
git clone https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend
```

In das Projektverzeichnis wechseln

```bash
cd wrodit-backend
```

Server starten

```bash
gradlew bootRun
```

## Anforderungen

* JDK 21

## Umgebungsvariablen

Um dieses Projekt auszuführen, müssen die folgenden Umgebungsvariablen gesetzt werden:

* `DB_URL`, Link zu deiner MySQL-Datenbank
* `DB_USERNAME`, Benutzername für den Datenbankbenutzer
* `DB_PASSWORD`, Passwort für den Datenbankbenutzer
* `SPRING_JPA_DDL_AUTO`, Hibernate ddl-auto Modus (normalerweise `update`)
* `JWT_SECRET`, das Secret zum Generieren von JWTs, mindestens 32 Zeichen lang
