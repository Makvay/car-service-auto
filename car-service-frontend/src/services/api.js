import axios from "axios";

const API_URL =
  process.env.REACT_APP_API_URL ||
  process.env.REACT_APP_API_GATEWAY ||
  "http://localhost:8090";
const KEYCLOAK_URL = process.env.REACT_APP_KEYCLOAK_URL || "http://localhost:8180";
const KEYCLOAK_REALM = process.env.REACT_APP_KEYCLOAK_REALM || "car-service";
const KEYCLOAK_CLIENT_ID = process.env.REACT_APP_KEYCLOAK_CLIENT || "car-service-frontend";
const SESSION_TTL_MS = 60 * 60 * 1000;

const api = axios.create({
  baseURL: API_URL
});

async function getToken() {
  const token = localStorage.getItem("kc_token");
  const expires = localStorage.getItem("kc_token_expires");

  if (!token || !expires) return null;
  if (Date.now() > Number.parseInt(expires, 10)) {
    return refreshToken();
  }
  return token;
}

async function refreshToken() {
  const storedRefreshToken = localStorage.getItem("kc_refresh_token");
  if (!storedRefreshToken) return null;

  try {
    const params = new URLSearchParams();
    params.append("client_id", KEYCLOAK_CLIENT_ID);
    params.append("refresh_token", storedRefreshToken);
    params.append("grant_type", "refresh_token");

    const response = await fetch(
      `${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token`,
      {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params
      }
    );

    if (!response.ok) return null;

    const data = await response.json();
    localStorage.setItem("kc_token", data.access_token);
    localStorage.setItem("kc_refresh_token", data.refresh_token);
    localStorage.setItem("kc_token_expires", Date.now() + SESSION_TTL_MS);
    
    return data.access_token;
  } catch {
    return null;
  }
}

api.interceptors.request.use(
  async (config) => {
    const token = await getToken();
    if (token) {
      config.headers = config.headers ?? {};
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    if (status === 401) {
      localStorage.removeItem("kc_token");
      localStorage.removeItem("kc_refresh_token");
      localStorage.removeItem("kc_token_expires");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;
