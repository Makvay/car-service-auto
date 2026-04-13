# Car Service Frontend

React (functional components + hooks) B2B dashboard for Car Service.

## Stack

- React 18 + react-router-dom
- Tailwind CSS (colors: black/white/orange, font: Inter)
- Keycloak (`keycloak-js`) + JWT
- Axios with interceptors
- Recharts for charts

## Run

```bash
cd car-service-frontend
npm install
npm start
```

App will be available at `http://localhost:3000`.

## Auth (Keycloak)

Keycloak config is in `src/services/keycloak.js`:

- url: `http://localhost:8090`
- realm: `car-service-realm`
- clientId: `car-service-frontend`

If user is not authenticated, `ProtectedRoute` triggers `keycloak.login()`.

## API

Gateway base URL: `http://localhost:8080` (`src/services/api.js`).

On each request: attaches `Authorization: Bearer <token>` and tries to refresh token.

On 401: triggers `keycloak.login()`.

