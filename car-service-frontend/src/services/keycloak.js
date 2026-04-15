import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8180",
  realm: "car-service",
  clientId: "frontend-client"
});

export const APP_ORIGIN = "http://localhost:3000";

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