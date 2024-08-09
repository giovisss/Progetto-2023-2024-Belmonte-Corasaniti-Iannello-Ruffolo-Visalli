package com.example.jokiandroid.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jokiandroid.R


class LoginActivity(navController: NavController) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Handle authentication result
        handleAuthenticationResult()
    }

    private fun handleAuthenticationResult() {
        val intent = intent
        val data = intent.data

        Log.e("res","data: $data")

        if (data != null && data.scheme == "joki" && data.host == "callback") {
            // Handle successful authentication
//            setContent() {
//                Surface {
//                    Text(
//                        modifier = Modifier.padding(top = 50.dp),
//                        text = "Login"
//                    )
//                }
//            }
        } else {
            // Handle error or cancellation
            Toast.makeText(this, "Authentication failed or cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}



@Composable
fun Login(navController: NavController) {
    Surface {
        Text( modifier = Modifier.padding(top = 50.dp),
            text = "Login"
        )
    }
}