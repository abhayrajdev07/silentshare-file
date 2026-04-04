package com.example.silentshare

import java.net.ServerSocket

class MessageServer(
    private val onMessageReceived: (String, String) -> Unit
) : Thread() {

    override fun run() {
        try {
            val serverSocket = ServerSocket(8888)

            while (true) {
                val socket = serverSocket.accept()

                val clientIP = socket.inetAddress.hostAddress

                val reader = socket.getInputStream().bufferedReader()
                val message = reader.readLine()

                if (message != null) {
                    onMessageReceived(message, clientIP ?: "")
                }

                socket.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}