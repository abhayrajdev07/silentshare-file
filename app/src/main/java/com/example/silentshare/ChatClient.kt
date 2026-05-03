package com.example.silentshare

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatClient {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var currentServerIp: String? = null
    private val _incomingMessages = MutableSharedFlow<String>()
    val incomingMessages = _incomingMessages.asSharedFlow()

    fun connectToServer(ipAddress: String) {
        if (ipAddress.isBlank()) return
        currentServerIp = ipAddress
        val request = Request.Builder().url("ws://$ipAddress:8080/chat").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                CoroutineScope(Dispatchers.IO).launch { _incomingMessages.emit(text) }
            }
        })
    }

    fun sendFile(uri: Uri, context: Context, category: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@launch
                inputStream.close()

                val fileName = getFileName(uri, context)

                // 👇 Convert to Base64 (offline safe)
                val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

                // 👇 Send actual data (NOT URI)
                sendMessage("$category|$base64|$fileName")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun getFileName(uri: Uri, context: Context): String {
        var name = "file_${System.currentTimeMillis()}"
        context.contentResolver.query(uri, null, null, null, null)?.use {
            val idx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && idx >= 0) name = it.getString(idx)
        }
        return name
    }

    fun sendMessage(msg: String) {
        webSocket?.send(msg)
    }
}