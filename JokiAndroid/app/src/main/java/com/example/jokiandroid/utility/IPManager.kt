package com.example.jokiandroid.utility

import android.content.Context
import android.util.Log
import com.example.jokiandroid.R

object IPManager {
    var KEYCLOAK_IP: String = ""
        set(value) {
            if (field == "") {
                field = value
            }else{
                throw Exception("Non puoi cambiare il valore di questa variabile")
            }
        }
    var BACKEND_IP: String = ""
        set(value) {
            if (field == "") {
                field = value
            }else{
                throw Exception("Non puoi cambiare il valore di questa variabile")
            }
        }

    var BACKEND_IMAGES : String = "NULL"
        set(value) {
            if (field == "NULL") {
                field = value
            }else{
                throw Exception("Non puoi cambiare il valore di questa variabile")
            }
        }

    fun setIps(context: Context){
        KEYCLOAK_IP = context.getString(R.string.keycloak)
        BACKEND_IP = context.getString(R.string.backend)
        BACKEND_IMAGES = "http://$BACKEND_IP/images"
    }
}