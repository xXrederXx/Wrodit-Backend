
# Wrodit

[![pipeline status](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/badges/main/pipeline.svg)](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/-/commits/main)

[![pipeline status](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/badges/production/pipeline.svg)](https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend/-/commits/production)

## Documentation

[Documentation](./docs)

## Run Locally

Clone the project

```bash
  git clone https://git.bbcag.ch/inf-bl/be/2025/team-h/bbrawn/fullstack/wrodit-backend
```

Go to the project directory

```bash
  cd wrodit-backend
```

Start the server

```bash
  gradlew bootRun
```

## Requirements

- JDK 21
  
## Environment Variables

To run this project, you will need to add the following environment variables.

`DB_URL`, link to your mysql database

`DB_USERNAME`, username for your database user

`DB_PASSWORD`, password for your database user

`SPRING_JPA_DDL_AUTO`, hibernate ddl-auto mode (normally update)

`JWT_SECRET`, the secret used to generate jwt's, at least 32 characters
