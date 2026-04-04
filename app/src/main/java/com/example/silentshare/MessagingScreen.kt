package com.example.silentshare

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.silentshare.ui.theme.BlueGradient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val text: String,
    val time: String,
    val isMe: Boolean
)

@Composable
fun MessagingScreen(
    userName: String,
    avatarUri: Uri?,
    hostAddress: String,
    isGroupOwner: Boolean, // 🔥 ADD THIS
    onBack: () -> Unit
) {


    var messageInput by remember { mutableStateOf("") }
    var lastClientIP by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val targetIP = if (isGroupOwner) {
        lastClientIP
    } else {
        hostAddress
    }

    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    // 🔥 RECEIVE MESSAGES
    LaunchedEffect(Unit) {
        MessageServer { msg, ip ->

            lastClientIP = ip // 🔥 STORE CLIENT IP

            messages.add(
                ChatMessage(
                    text = msg,
                    time = getCurrentTime(),
                    isMe = false
                )
            )

        }.start()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
    ) {

        TopBar(
            userName = userName,
            avatarUri = avatarUri,
            onBack = onBack
        )

        // 🔹 MESSAGE LIST
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { msg ->
                MessageBubble(msg)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // 🔹 INPUT
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = messageInput,
                onValueChange = { messageInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type message") },
                shape = RoundedCornerShape(23.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {

                    if (messageInput.isNotEmpty()) {

                        // ✅ CORRECT SEND
                        MessageSender().sendMessage(
                            hostAddress,
                            messageInput
                        )

                        // ✅ ADD TO UI
                        messages.add(
                            ChatMessage(
                                text = messageInput,
                                time = getCurrentTime(),
                                isMe = true
                            )
                        )

                        messageInput = ""
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start
    ) {

        Column(
            modifier = Modifier
                .background(
                    if (message.isMe) Color(0xFF005C4B) else Color(0xFF202C33),
                    RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
                .widthIn(max = 250.dp)
        ) {

            Text(message.text, color = Color.White)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                message.time,
                color = Color.LightGray,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}