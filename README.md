# Wrodit

**Main**

[![Pipeline-Status](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/badges/main/pipeline.svg)](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/-/commits/main)

[![coverage report](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/badges/main/coverage.svg)](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/-/commits/main)

**Production**

[![Pipeline-Status](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/badges/production/pipeline.svg)](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/-/commits/production)


[![coverage report](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/badges/production/coverage.svg)](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/-/commits/production)

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
