package com.example.jokiandroid.config

object AuthConfig {
    const val CLIENT_ID = "JokiClient"
    const val REDIRECT_URI = "joki://login"

    //DISCOVERY_URI è una costante che contiene l'URL per il documento di configurazione di Keycloak. Questo URL segue il formato .well-known/openid-configuration e
    // viene utilizzato per ottenere dinamicamente i dettagli di configurazione di Keycloak, come gli endpoint di autorizzazione, token, ecc. Utilizzando questo URL,
    // non è necessario specificare manualmente gli endpoint di autorizzazione e token.

    const val DISCOVERY_URI = "http://192.168.1.6:8080/realms/JokiRealm/.well-known/openid-configuration"
}
