package com.example.silentshare

import android.os.Environment
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.consumeEach
import java.io.File
import java.util.Collections

object LocalServer {
    private var server: ApplicationEngine? = null
    private val clients = Collections.synchronizedSet<WebSocketSession>(LinkedHashSet())

    fun startServer() {
        if (server != null) return
        server = embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
            install(WebSockets)
            routing {
                webSocket("/chat") {
                    clients.add(this)
                    try {
                        incoming.consumeEach { frame ->
                            if (frame is Frame.Text) {
                                val msg = frame.readText()
                                clients.filter { it != this@webSocket }.forEach { it.send(msg) }
                            }
                        }
                    } finally {
                        clients.remove(this)
                    }
                }
                post("/upload") {
                    val multipart = call.receiveMultipart()
                    var category = "file"
                    multipart.forEachPart { part ->
                        if (part is PartData.FormItem && part.name == "category") category =
                            part.value
                        if (part is PartData.FileItem) {
                            val fileName =
                                part.originalFileName ?: "file_${System.currentTimeMillis()}"
                            val file = File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                fileName
                            )
                            part.streamProvider().use { input ->
                                file.outputStream().buffered().use { input.copyTo(it) }
                            }
                            val path = file.absolutePath
                            clients.forEach { it.send("$category|$path|$fileName") }
                        }
                        part.dispose()
                    }
                    call.respond(HttpStatusCode.OK)
                }
            }
        }.start(wait = false)
    }

    fun stopServer() {
        server?.stop(1000, 2000)
        server = null
        clients.clear()
    }
}