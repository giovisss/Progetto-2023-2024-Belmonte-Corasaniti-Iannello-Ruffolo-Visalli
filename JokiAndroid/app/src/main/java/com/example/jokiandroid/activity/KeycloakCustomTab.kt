package com.example.jokiandroid.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable

@Composable
fun KeycloakCustomTab(context: Context, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        try {
            customTabsIntent.launchUrl(context, Uri.parse(url))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Nessun browser compatibile trovato", Toast.LENGTH_SHORT).show()
        }

}




