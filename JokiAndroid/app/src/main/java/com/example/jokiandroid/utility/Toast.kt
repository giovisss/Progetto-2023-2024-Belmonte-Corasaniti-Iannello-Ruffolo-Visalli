package com.example.jokiandroid.utility

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jokiandroid.R

object ToastString {
    private val _message = MutableLiveData<String>("")
    val message: LiveData<String> get() = _message

    fun setMessage(message: String) {
        _message.value = ""
        _message.value = message
    }
}

@Composable
fun CustomToast(context: Context) {
    val message by ToastString.message.observeAsState()

    if (message == "") return

    val inflater = LayoutInflater.from(context)
    val layout: View = inflater.inflate(R.layout.custom_toast, null)

    val text: TextView = layout.findViewById(R.id.toast_text)
    text.text = message

    with(Toast(context)) {
        duration = Toast.LENGTH_SHORT
        view = layout
        show()
    }
}
