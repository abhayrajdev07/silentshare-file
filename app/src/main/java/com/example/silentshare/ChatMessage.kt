package com.example.silentshare

enum class MessageStatus { SENDING, SENT, DELIVERED, READ }

// 👇 NEW: Message type add karo
enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    FILE
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),

    // TEXT message
    val text: String? = null,

    // FILE / IMAGE
    val fileUri: String? = null,   // 👈 IMPORTANT (image preview ke liye)
    val fileName: String? = null,  // 👈 filename alag rakho
    val fileType: MessageType = MessageType.TEXT, // 👈 TYPE CONTROL

    val isSentByMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENDING
)