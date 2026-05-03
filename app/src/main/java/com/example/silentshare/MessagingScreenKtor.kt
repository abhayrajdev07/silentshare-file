package com.example.silentshare

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.silentshare.ui.theme.BlueGradient
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// ── Color tokens ─────────────────────────────────────────────────────────────
private val SentBubble = Color(0xFFE7FFDB)
private val ReceivedBubble = Color(0xFFFFFFFF)
private val AppBarBg = Color(0xFF0A6E5C)        // deep teal
private val AppBarText = Color.White
private val TickSent = Color(0xFF8A9BA8)        // grey tick
private val TickRead = Color(0xFF53BDEB)        // blue tick
private val TimestampColor = Color(0xFF8E9BAB)
private val InputBg = Color(0xFFF0F2F5)
private val SendButtonColor = Color(0xFF00A884)
private val ChatWallpaper = Color(0xFFECE5DD)        // WhatsApp-ish warm grey

// ── Time formatter ────────────────────────────────────────────────────────────
private val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
private fun formatTime(millis: Long): String = timeFormat.format(Date(millis))

// ─────────────────────────────────────────────────────────────────────────────
//  MessagingScreenKtor
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun MessagingScreenKtor(
    userName: String,
    avatarUri: Uri?,
    serverIp: String,
    isServer: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var text by remember { mutableStateOf("") }
    val chatClient = remember { ChatClient() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom on new message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    // ── File picker ──────────────────────────────────────────────────────────
    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val type = context.contentResolver.getType(it) ?: ""
            val cat = when {
                type.contains("image") -> "image"
                type.contains("video") -> "video"
                else -> "file"
            }
            messages.add(
                ChatMessage(
                    text = "Sending…",
                    fileUri = it.toString(),
                    fileType = cat,
                    isSentByMe = true,
                    status = MessageStatus.SENDING
                )
            )
            scope.launch { chatClient.sendFile(it, context, cat) }
        }
    }

    // ── Network events ───────────────────────────────────────────────────────
    LaunchedEffect(isServer, serverIp) {
        chatClient.connectToServer(if (isServer) "127.0.0.1" else serverIp)
        chatClient.incomingMessages.collect { msg ->
            val parts = msg.split("|")
            if (parts.size >= 3) {
                val (category, uri, fileName) = parts
                val isMyMessage = uri.startsWith("content://")
                val existingIdx = messages.indexOfLast { it.isSentByMe && it.text == "Sending…" }

                if (isMyMessage && existingIdx != -1) {
                    messages[existingIdx] = messages[existingIdx].copy(
                        fileType = category,
                        fileUri = uri,
                        text = fileName,
                        status = MessageStatus.SENT
                    )
                } else if (!isMyMessage) {
                    messages.add(
                        ChatMessage(
                            fileType = category,
                            fileUri = uri,
                            text = fileName,
                            isSentByMe = false,
                            status = MessageStatus.DELIVERED
                        )
                    )
                }
            } else {
                messages.add(
                    ChatMessage(
                        text = msg,
                        isSentByMe = false,
                        status = MessageStatus.DELIVERED
                    )
                )
            }
        }
    }

    // ── Root layout ──────────────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Top App Bar
        ModernTopBar(userName = userName, avatarUri = avatarUri, onBack = onBack)

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(messages, key = { it.timestamp }) { msg ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically { it / 2 }
                ) {
                    ChatBubble(msg)
                }
            }
        }

        // Input bar
        ModernInputBar(
            text = text,
            onTextChange = { text = it },
            onAttach = { filePicker.launch("*/*") },
            onSend = {
                if (text.isNotBlank()) {
                    chatClient.sendMessage(text)
                    messages.add(
                        ChatMessage(
                            text = text,
                            isSentByMe = true,
                            status = MessageStatus.SENT
                        )
                    )
                    text = ""
                }
            }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Top App Bar
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ModernTopBar(
    userName: String,
    avatarUri: Uri?,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Transparent
            )
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFF25D366))
        ) {
            if (avatarUri != null) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = userName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        Column {
            Text(
                text = userName,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "online",
                color = Color(0xFFB2EBD6),
                fontSize = 12.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Modern Input Bar
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ModernInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onAttach: () -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text field pill
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(25.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = text,
                    onValueChange = onTextChange,
                    placeholder = { Text("Message", color = Color(0xFFADB5BD), fontSize = 15.sp) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Color(0xFF128C7E),
                        focusedTextColor = Color(0xFF111827),
                        unfocusedTextColor = Color(0xFF374151)
                    ),
                    maxLines = 4
                )
                IconButton(onClick = onAttach) {
                    Icon(
                        Icons.Default.AttachFile,
                        contentDescription = "Attach",
                        tint = Color(0xFF6B7280)
                    )
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        // Send FAB
        IconButton(
            onClick = onSend,
            modifier = Modifier
                .size(48.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF25D366), Color(0xFF00A884))
                    ),
                    CircleShape
                )
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Chat Bubble
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ChatBubble(message: ChatMessage) {
    val context = LocalContext.current
    val alignment = if (message.isSentByMe) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isSentByMe) SentBubble else ReceivedBubble

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = if (message.isSentByMe) 16.dp else 4.dp,
                topEnd = if (message.isSentByMe) 4.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            shadowElevation = 1.dp,
            modifier = Modifier
                .widthIn(min = 80.dp, max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {

                // ── Media / text content ─────────────────────────────────────
                if ((message.fileType == "image" || message.fileType == "video") && message.fileUri != null) {
                    val model = remember(message.fileUri) {
                        if (message.fileUri.startsWith("/"))
                            File(message.fileUri)
                        else
                            message.fileUri.toUri()
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
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                },
                            contentScale = ContentScale.Crop
                        )
                        if (message.fileType == "video") {
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(52.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    message.text?.let {
                        if (it != "Sending…") {
                            Text(
                                text = it,
                                fontSize = 13.sp,
                                color = Color(0xFF374151)
                            )
                        }
                    }
                } else {
                    message.text?.let {
                        Text(
                            text = it,
                            fontSize = 15.sp,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                // ── Timestamp + ticks row ────────────────────────────────────
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = formatTime(message.timestamp),
                        fontSize = 10.sp,
                        color = TimestampColor,
                        fontWeight = FontWeight.Normal
                    )
                    if (message.isSentByMe) {
                        MessageTickIcon(status = message.status)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Tick Icon
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun MessageTickIcon(status: MessageStatus) {
    val (icon, tint) = when (status) {
        MessageStatus.SENDING -> Icons.Default.Done to TickSent   // single grey
        MessageStatus.SENT -> Icons.Default.Done to TickSent   // single grey
        MessageStatus.DELIVERED -> Icons.Default.DoneAll to TickSent   // double grey
        MessageStatus.READ -> Icons.Default.DoneAll to TickRead   // double blue
    }
    Icon(
        imageVector = icon,
        contentDescription = status.name,
        tint = tint,
        modifier = Modifier.size(15.dp)
    )
}