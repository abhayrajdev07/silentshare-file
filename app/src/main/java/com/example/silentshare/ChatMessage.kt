package com.example.silentshare

enum class MessageStatus { SENDING, SENT, DELIVERED, READ }
data class ChatMessage(
    val text: String? = null,
    val fileUri: String? = null,
    val fileType: String? = null,
    val isSentByMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENDING
)