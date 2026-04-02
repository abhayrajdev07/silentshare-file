package com.example.silentshare

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.net.Socket

class MessageSender {

    fun sendMessage(host: String, message: String) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = Socket(host, 8888)
                val writer = PrintWriter(socket.getOutputStream(), true)

                writer.println(message)
                writer.flush()

                socket.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
