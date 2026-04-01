# User Acceptance Test

## UAT 01 - Registrierung

|               |                                                                                                                                                                                                                             |
| ------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ID            | UAT 01                                                                                                                                                                                                                      |
| User Story    | US 01                                                                                                                                                                                                                       |
| Voraussetzung | Der user Ist auf der Regisrtierungsseite [http://g8o804o8o84g84w400c8gssk.207.180.221.9.sslip.io/wrodit/register](http://g8o804o8o84g84w400c8gssk.207.180.221.9.sslip.io/wrodit/register)                                   |
| Ablauf        | User öffnet [Regisrtierungsseite](http://g8o804o8o84g84w400c8gssk.207.180.221.9.sslip.io/wrodit/register), gibt Daten ein, Bennutzername: testUser und Email: test@user.ch Passwort: Admin123+ undd clickt auf registrieren |
| Erwartet      | User kann Account erstellen und wird auf die Anmeldeseite weitergeleitet                                                                                                                                                    |

## UAT 02 - Registrierung

|               |                                                                                                                                                                                                                             |
| ------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ID            | UAT 02                                                                                                                                                                                                                      |
| User Story    | US 01                                                                                                                                                                                                                       |
| Voraussetzung | Der user Ist auf der [Regisrtierungsseite](http://g8o804o8o84g84w400c8gssk.207.180.221.9.sslip.io/wrodit/register) und die US1 ist abgeschossen beziehungsweiser testUser Existiert                                         |
| Ablauf        | User öffnet [Regisrtierungsseite](http://g8o804o8o84g84w400c8gssk.207.180.221.9.sslip.io/wrodit/register), gibt Daten ein, Bennutzername: testUser und Email: tset@user.ch Passwort: Admin123+ undd clickt auf registrieren |
| Erwartet      | User kann kein Account erstellen und eine Fehlermeldung wird angezeigt                                                                                                                                                      |

## UAT 03 - Login

|               |                                                                                                                                       |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| ID            | UAT 03                                                                                                                                |
| User Story    | US 02                                                                                                                                 |
| Voraussetzung | Registrierter Account(UAT1) befindet sich auf der [Anmeldeseite](http://g8o804o8o84g84w400c8gssk.207.180.221.9.sslip.io/wrodit/login) |
| Ablauf        | User gibt Benutzername: testUser und Passwort: Admin123+ ein                                                                          |
| Erwartet      | User kann sich einloggen und wird auf die Homeseite weitergeleitet                                                                    |

## UAT 04 - Login

|               |                                                                                                                                  |
| ------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| ID            | UAT 04                                                                                                                           |
| User Story    | US 02                                                                                                                            |
| Voraussetzung | Registrierter Account, befindet sich auf der [Anmeldeseite](http://g8o804o8o84g84w400c8gssk.207.180.221.9.sslip.io/wrodit/login) |
| Ablauf        | User gibt Benutzername: testUser und Passwort: Admin ein                                                                         |
| Erwartet      | User kann sich nicht einloggen und ene Meldung wird Angezeigt Username oder Passwort falsch                                      |

## UAT 05 - Profilseite

|               |                                                                        |
| ------------- | ---------------------------------------------------------------------- |
| ID            | UAT 05                                                                 |
| User Story    | US 09                                                                  |
| Voraussetzung | Profil existiert UAT1/UAT3 abgeschlossen                               |
| Ablauf        | User öffnet Profilseite im header auf dem profil Icon unter Mein Konto |
| Erwartet      | Profilinformationen Bennutzername und E-Mail werden angezeigt          |

## UAT 06 - Thread erstellen

|               |                                                                                                                                                                                                             |
| ------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ID            | UAT 06                                                                                                                                                                                                      |
| User Story    | US 08                                                                                                                                                                                                       |
| Voraussetzung | Angemeldeter User und UAT5                                                                                                                                                                                  |
| Ablauf        | User navigiert zu seinem profil im header auf dem profil Icon unter Mein Konto und clickt dort auf Thread erstellen, User erstellt Thread mit Name: **Test thread** und Beschreibung: **test beschreibung** |
| Erwartet      | Neuer Thread wird erstellt in seinem Profil immer mit w/Threadname gekennzeicnet                                                                                                                            |

## UAT 07 - Thread erstellen

|               |                                                                                                                                 |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| ID            | UAT 07                                                                                                                          |
| User Story    | US 08                                                                                                                           |
| Voraussetzung | Angemeldeter User und UAT5                                                                                                      |
| Ablauf        | User navigiert zu seinem profil und clickt dort auf Thread erstellen, User gibt keine Daten ein und Clickt auf Thread erstellen |
| Erwartet      | Neuer Thread wird nicht erstellt fehlermeldung                                                                                  |

## UAT 08 - Post erstellen

|               |                                                                          |
| ------------- | ------------------------------------------------------------------------ |
| ID            | UAT 08                                                                   |
| User Story    | US 07                                                                    |
| Voraussetzung | Eingeloggter User und vorhandener Thread                                 |
| Ablauf        | User öffnet einen Thread, erstellt einen neuen Post und speichert diesen |
| Erwartet      | Neuer Post wird erfolgreich erstellt und im Thread angezeigt             |

## UAT 09 - Post erstellen

|               |                                                                       |
| ------------- | --------------------------------------------------------------------- |
| ID            | UAT 09                                                                |
| User Story    | US 07                                                                 |
| Voraussetzung | Eingeloggter User und vorhandener Thread                              |
| Ablauf        | User öffnet einen Thread, versucht neuer post ohne Daten zu erstellen |
| Erwartet      | Neuer Post wird nicht erstellt und FehlerMeldung Erscheint            |

## UAT 10 - Homeseite anzeigen

|               |                                                          |
| ------------- | -------------------------------------------------------- |
| ID            | UAT 10                                                   |
| User Story    | US 03                                                    |
| Voraussetzung | Eingeloggter User und vorhandene Threads mit Posts       |
| Ablauf        | User klickt auf das Home-Icon und öffnet die Homeseite   |
| Erwartet      | Alle relevanten Posts werden auf der Homeseite angezeigt |

## UAT 11 - Thread Seite

|               |                                                                |
| ------------- | -------------------------------------------------------------- |
| ID            | UAT 11                                                         |
| User Story    | US 05                                                          |
| Voraussetzung | Thread existiert und enthält mindestens einen Post             |
| Ablauf        | User öffnet einen Thread uber den link beim ensprechenden post |
| Erwartet      | Alle Posts des Threads werden korrekt angezeigt                |

## UAT 12 - Post Detailansicht

|               |                                                           |
| ------------- | --------------------------------------------------------- |
| ID            | UAT 12                                                    |
| User Story    | US 04                                                     |
| Voraussetzung | Post existiert                                            |
| Ablauf        | User klickt auf einen Post                                |
| Erwartet      | Post wird auf einer eigenen Detailseite korrekt angezeigt |

## UAT 13 - Kommentieren

|               |                                                                |
| ------------- | -------------------------------------------------------------- |
| ID            | UAT 13                                                         |
| User Story    | US 06                                                          |
| Voraussetzung | Eingeloggter User und existierender Post                       |
| Ablauf        | User schreibt bei einem Post einen Kommentar und sendet diesen |
| Erwartet      | Kommentar wird gespeichert und unter dem Post angezeigt        |

## UAT 14 - Kommentare anzeigen

|               |                                                 |
| ------------- | ----------------------------------------------- |
| ID            | UAT 14                                          |
| User Story    | US 16                                           |
| Voraussetzung | Post mit vorhandenen Kommentaren                |
| Ablauf        | User öffnet die Detailansicht eines Posts       |
| Erwartet      | Alle Kommentare werden unter dem Post angezeigt |

## UAT 15 - Likes / Dislikes

|               |                                                                 |
| ------------- | --------------------------------------------------------------- |
| ID            | UAT 15                                                          |
| User Story    | US 10                                                           |
| Voraussetzung | Posts oder Kommentare existieren                                |
| Ablauf        | User klickt auf Like oder Dislike bei einem Post oder Kommentar |
| Erwartet      | Bewertung wird gespeichert und korrekt angezeigt                |

## UAT 16 - Link kopieren

|               |                                                                           |
| ------------- | ------------------------------------------------------------------------- |
| ID            | UAT 16                                                                    |
| User Story    | US 11                                                                     |
| Voraussetzung | Post existiert                                                            |
| Ablauf        | User klickt auf den „Copy-Link“-Button bei einem Postund kopiert den link |
| Erwartet      | Link wird in die Zwischenablage kopiert                                   |

## UAT 17 - Post löschen

|               |                                                                      |
| ------------- | -------------------------------------------------------------------- |
| ID            | UAT 17                                                               |
| User Story    | US 12                                                                |
| Voraussetzung | Eigener Post existiert                                               |
| Ablauf        | User klickt auf „Löschen“ bei einem eigenen Post auf der Profilseite |
| Erwartet      | Post wird gelöscht und nicht mehr angezeigt                          |

## UAT 18 - Account löschen

|               |                                                                    |
| ------------- | ------------------------------------------------------------------ |
| ID            | UAT 18                                                             |
| User Story    | US 13                                                              |
| Voraussetzung | Eingeloggter User befindet sich auf seinem Account                 |
| Ablauf        | User klickt auf „Account löschen“ und bestätigt die Aktion         |
| Erwartet      | Account wird gelöscht und User wird ausgeloggt bzw. weitergeleitet |

## UAT 19 - Markdown schreiben

|               |                                                                      |
| ------------- | -------------------------------------------------------------------- |
| ID            | UAT 19                                                               |
| User Story    | US 14                                                                |
| Voraussetzung | Post-Editor ist geöffnet                                             |
| Ablauf        | User schreibt einen Post mit Markdown (# hallo) und speichert diesen |
| Erwartet      | Markdown wird korrekt gespeichert                                    |

## UAT 20 - Markdown anzeigen

|               |                                                                          |
| ------------- | ------------------------------------------------------------------------ |
| ID            | UAT 20                                                                   |
| User Story    | US 15                                                                    |
| Voraussetzung | Post mit Markdown-Inhalt existiert                                       |
| Ablauf        | User öffnet den Post                                                     |
| Erwartet      | Markdown wird korrekt formatiert dargestellt, hallo wirt gross angezeigt |

## UAT 21 - Content Filter

|               |                                                                    |
| ------------- | ------------------------------------------------------------------ |
| ID            | UAT 21                                                             |
| User Story    | US 17                                                              |
| Voraussetzung | User erstellt einen neuen Post                                     |
| Ablauf        | User schreibt einen Post mit Inhalt Fuck und versucht zu speichern |
| Erwartet      | Post wird blockiert und eine Fehlermeldung angezeigt               |

## UAT 22 - Inhalte bearbeiten

|               |                                                                                |
| ------------- | ------------------------------------------------------------------------------ |
| ID            | UAT 22                                                                         |
| User Story    | US 18                                                                          |
| Voraussetzung | Eigener Post existiert und User befindet sich auf der Detail- oder Profilseite |
| Ablauf        | User bearbeitet den Post und speichert die Änderungen                          |
| Erwartet      | Änderungen werden gespeichert und aktualisiert angezeigt                       |

## UAT 23 - Thread Banner / Icon (Optional)

|               |                                |
| ------------- | ------------------------------ |
| ID            | UAT 23                         |
| User Story    | US 19                          |
| Voraussetzung | Thread erstellen               |
| Ablauf        | User lädt Banner und Icon hoch |
| Erwartet      | Bilder werden gespeichert      |

## UAT 24 - Profilbild hochladen (Optional)

|               |                             |
| ------------- | --------------------------- |
| ID            | UAT 24                      |
| User Story    | US 20                       |
| Voraussetzung | Login                       |
| Ablauf        | User lädt Profilbild hoch   |
| Erwartet      | Profilbild wird gespeichert |

## UAT 25 - Suche (Optional)

|               |                                 |
| ------------- | ------------------------------- |
| ID            | UAT 25                          |
| User Story    | US 21                           |
| Voraussetzung | Inhalte existieren              |
| Ablauf        | User gibt Suchbegriff ein       |
| Erwartet      | Suchergebnisse werden angezeigt |
