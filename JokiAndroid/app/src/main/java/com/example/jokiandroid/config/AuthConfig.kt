package com.example.jokiandroid.config

import com.example.jokiandroid.utility.IPManager

object AuthConfig {
    const val CLIENT_ID = "JokiClient"
    const val REDIRECT_URI = "joki://login"

    //DISCOVERY_URI è una costante che contiene l'URL per il documento di configurazione di Keycloak. Questo URL segue il formato .well-known/openid-configuration e
    // viene utilizzato per ottenere dinamicamente i dettagli di configurazione di Keycloak, come gli endpoint di autorizzazione, token, ecc. Utilizzando questo URL,
    // non è necessario specificare manualmente gli endpoint di autorizzazione e token.

    var DISCOVERY_URI = "http://${IPManager.KEYCLOAK_IP}/realms/JokiRealm/.well-known/openid-configuration"
        set(value) {
            throw Exception("Non puoi cambiare il valore di questa variabile")
        }
}
