package com.example.silentshare

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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
        val serverIp = currentServerIp ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fileName = getFileName(uri, context)
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@launch
                inputStream.close()

                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        fileName,
                        bytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                    )
                    .addFormDataPart("category", category)
                    .build()

                val request =
                    Request.Builder().url("http://$serverIp:8080/upload").post(body).build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        // Notify others: category|uri|fileName
                        sendMessage("$category|$uri|$fileName")
                    }
                }
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