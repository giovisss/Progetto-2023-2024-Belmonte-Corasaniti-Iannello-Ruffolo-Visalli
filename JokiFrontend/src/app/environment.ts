import { BASE_KEYCLOAK_URL } from "./global";

export const environment = {
  production: false,
  keycloak: {
    authority: BASE_KEYCLOAK_URL,
    redirectUri: 'http://localhost:4200',
    postLogoutRedirectUri: 'http://localhost:4200/logout',
    realm: 'JokiRealm',
    clientId: 'JokiClient',
  },
};
