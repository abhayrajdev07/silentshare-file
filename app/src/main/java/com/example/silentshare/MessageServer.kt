package com.example.silentshare

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket

class MessageServer(private val onMessageReceived: (String) -> Unit) {

    fun startServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val serverSocket = ServerSocket(8888)

                while (true) {

                    val client = serverSocket.accept()

                    val reader = BufferedReader(InputStreamReader(client.getInputStream()))
                    val message = reader.readLine()

                    if (message != null) {
                        onMessageReceived(message)
                    }

                    client.close()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
