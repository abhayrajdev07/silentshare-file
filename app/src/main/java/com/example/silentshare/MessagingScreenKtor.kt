package com.example.silentshare

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.silentshare.ui.theme.BlueGradient
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MessagingScreenKtor(
    userName: String,
    avatarUri: Uri?,
    serverIp: String,
    isServer: Boolean,
    onBack: () -> Unit
) {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var text by remember { mutableStateOf("") }
    val chatClient = remember { ChatClient() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val filePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val type = context.contentResolver.getType(it) ?: ""
                val cat =
                    if (type.contains("image")) "image" else if (type.contains("video")) "video" else "file"
                // Add local bubble immediately
                messages.add(
                    ChatMessage(
                        text = "Sending...",
                        fileUri = it.toString(),
                        fileType = cat,
                        isSentByMe = true
                    )
                )
                scope.launch { chatClient.sendFile(it, context, cat) }
            }
        }

    LaunchedEffect(isServer, serverIp) {
        chatClient.connectToServer(if (isServer) "127.0.0.1" else serverIp)
        chatClient.incomingMessages.collect { msg ->
            val parts = msg.split("|")
            if (parts.size >= 3) {
                val category = parts[0]
                val uri = parts[1]
                val fileName = parts[2]

                // Duplicate Check: My own message check
                val isMyMessage = uri.startsWith("content://")
                val existingIdx = messages.indexOfLast { it.isSentByMe && it.text == "Sending..." }

                if (isMyMessage && existingIdx != -1) {
                    // Update my existing bubble
                    messages[existingIdx] = messages[existingIdx].copy(
                        fileType = category,
                        fileUri = uri,
                        text = fileName
                    )
                } else if (!isMyMessage) {
                    // Add as receiver bubble
                    messages.add(
                        ChatMessage(
                            fileType = category,
                            fileUri = uri,
                            text = fileName,
                            isSentByMe = false
                        )
                    )
                }
            } else {
                messages.add(ChatMessage(text = msg, isSentByMe = false))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        TopBar(userName, avatarUri, onBack)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            items(messages) { ChatBubble(it) }
        }
//        Bottom Bar Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { filePicker.launch("*/*") }) {
                Icon(
                    Icons.Default.AttachFile,
                    null
                )
            }
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Type a Message..", color = Color.Gray) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.Gray
                )
            )
            IconButton(onClick = {
                if (text.isNotBlank()) {
                    chatClient.sendMessage(text); messages.add(
                        ChatMessage(
                            text = text,
                            isSentByMe = true
                        )
                    ); text = ""
                }
            }, modifier = Modifier.background(Color(0xFF00A884), CircleShape)) {
                Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White)
            }
        }
    }
}

@Composable
// h
fun ChatBubble(message: ChatMessage) {
    val context = LocalContext.current
    val alignment = if (message.isSentByMe) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), contentAlignment = alignment
    ) {
        Surface(
            color = if (message.isSentByMe) Color(0xFFDCF8C6) else Color.White,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 260.dp)
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                if ((message.fileType == "image" || message.fileType == "video") && message.fileUri != null) {
                    val model = remember(message.fileUri) {
                        if (message.fileUri!!.startsWith("/")) File(message.fileUri!!) else message.fileUri!!.toUri()
                    }

                    Box(contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = model,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            val uri =
                                                if (model is File) Uri.fromFile(model) else model as Uri
                                            setDataAndType(
                                                uri,
                                                if (message.fileType == "image") "image/*" else "video/*"
                                            )
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }; context.startActivity(intent)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                },
                            contentScale = ContentScale.Crop
                        )
                        if (message.fileType == "video") Icon(
                            Icons.Default.PlayCircle,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                } else {
                    message.text?.let {
                        Text(
                            it,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}