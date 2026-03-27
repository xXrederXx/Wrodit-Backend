# Production

Die Production Umgebungen werden von den Usern benutzt. Es ist die Produktive version unseres codes. Der Code muss alle anforderungen erfüllen und sollte keine halb fertigen features enthalten.

Diese Umgebungen müssen mit mehr sorgfallt behandelt werden.

Der Code kann veraltet sein, dah es erst ein update gibt, sobald ein neuer teil des Codes abgeschlossen wurde.

## Frontend

- URL: http://ag0ow0k4o48ocks0c4ogsgos.207.180.221.9.sslip.io
- Build Pack: Nixpacks
- Build Command: npm run buildProd

### Enviroment Variables

- NIXPACKS_NODE_VERSION: `<node_version>`
- VITE_API_URL: `<backend_url>`

## Backend

- URL: http://vcg00wk8ws8o0gcc4c8ckkgw.207.180.221.9.sslip.io
- Build Pack: Dockerfile

### Enviroment Variables

- DB_URL: `<database_url>`
- DB_USERNAME: `<database_username>`
- DB_PASSWORD: `<database_password>`
- JWT_SECRET: `<secret_for_jwt_generation>`
