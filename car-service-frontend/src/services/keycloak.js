import Keycloak from "keycloak-js";

const KEYCLOAK_URL = process.env.REACT_APP_KEYCLOAK_URL || "http://localhost:8180";
const KEYCLOAK_REALM = process.env.REACT_APP_KEYCLOAK_REALM || "car-service";
const KEYCLOAK_CLIENT = process.env.REACT_APP_KEYCLOAK_CLIENT || "car-service-frontend";
const APP_ORIGIN = process.env.REACT_APP_APP_ORIGIN || "http://localhost:3000";

const keycloak = new Keycloak({
  url: KEYCLOAK_URL,
  realm: KEYCLOAK_REALM,
  clientId: KEYCLOAK_CLIENT
});
export { APP_ORIGIN };

let initPromise = null;
let initResult = null;
let initError = null;

export function initKeycloak() {
  if (initPromise) return initPromise;
  if (initResult != null) return Promise.resolve(initResult);
  if (initError != null) return Promise.reject(initError);

  const initCall = keycloak.init({
    onLoad: "login-required",
    pkceMethod: "S256",
    checkLoginIframe: false,
    redirectUri: `${APP_ORIGIN}/`
  });

  const timeoutMs = 10000;
  const withTimeout = Promise.race([
    initCall,
    new Promise((_, reject) =>
      setTimeout(
        () =>
          reject(
            new Error(
              `Keycloak init timeout (${timeoutMs}ms). Проверь доступность http://localhost:8180 и настройки клиента.`
            )
          ),
        timeoutMs
      )
    )
  ]);

  initPromise = withTimeout
    .then((authenticated) => {
      initResult = authenticated;
      return authenticated;
    })
    .catch((e) => {
      initError = e;
      throw e;
    })
    .finally(() => {
      if (initResult == null) initPromise = null;
    });

  return initPromise;
}

export default keycloak;
