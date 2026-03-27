# Konfiguration

## .gitignore

Die `.gitignore` Datei enthält Ordner und Dateien welche nicht in GIT aufgenommen werden sollten. Die grundlage wurde wurde generieret und anschliessend leicht angepasst, für Projektspezifische Daten.

- [topal generator](https://www.toptal.com/developers/gitignore)
- [generiertes gitignore](https://www.toptal.com/developers/gitignore/api/visualstudiocode,intellij+all,java,git)

## .gitattributes

Die `.gitattributes` Datei enthält anweisungen an GIT wie es beschtimmte dateien behandeln soll. In unserem fall macht es folgendes.

Es setzt die Zeilen enden bei `/gradlew` zu `lf` und sagt, dass es eine text datei ist.

Es setzt den typ von `.bat` dateien auf text und stellt sicher, dass die Zeilen enden `crlf` sind.

Alle `.jar` Dateien werden als binärdateien dargeschtellt.

## .gitlab-ci.yml

Dies sind die CI/CD pipelines. Sie werden in folgenden Dokumenten genauer beschrieben.

- [Backend](./ci-cd/backend.md)
- [Frontend](./ci-cd/frontend.md)

## Dockerfile

Das dockerfile ist eine anleitung für docker. Sie besagt, wie es einen Container starten kann und wie er konfigurert sein muss.

In unserem fall, kompiliert er zuerst das Backend und startet es anschliessend. auserdem, wird der port 8080 freigegeben.

## .dockerignore

Die `.dockerignore` Datei funktioniert ähnlich wie die `.gitignore` Datei, nur für docker. Es beinhaltet Ordner, welche von docker ignoriert werden können. Dies macht docker schneller.

## Enviroment Variables

Die Enviroment Variables können auf verschiedene weisen eingeschtellt werdem. Welche benutzt werden, ist in folgenden Dateien beschrieben:

- [Production](./production.md)
- [Staging](./staging.md)