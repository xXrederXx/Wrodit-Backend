# Staging

Die Staging Umgebungen sind immer auf dem neusten stand. Sie benutzen den neusten code. Dies führt dazu, dass sie perfekt sind zum testen ausserhalb von localhost.

Die Umgebungen sind besonders hilfreich fehler bei der Komunikation von Frontend und Backend (z.B CORS) zu finden. Ausserdem kann man die Konfiguration von den Umgebungen ohne bedenken ändern um zu sehen was passiert, bzw. ob es ein Problem löst.

## Frontend

- URL: http://g8o804o8o84g84w400c8gssk.207.180.221.9.sslip.io/
- Build Pack: Nixpacks
- Build Command: npm run buildStage

### Enviroment Variables

- NIXPACKS_NODE_VERSION: `<node_version>`
- VITE_API_URL: `<backend_url>`

## Backend

- URL: http://xcwkwswkso04gs40k8g48k8w.207.180.221.9.sslip.io/
- Build Pack: Dockerfile

### Enviroment Variables

- DB_URL: `<database_url>`
- DB_USERNAME: `<database_username>`
- DB_PASSWORD: `<database_password>`
- JWT_SECRET: `<secret_for_jwt_generation>`
