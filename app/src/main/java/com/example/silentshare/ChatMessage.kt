package com.example.silentshare

data class ChatMessage(
    val sender: String = "User",
    val text: String? = null,
    val fileUri: String? = null,
    val fileType: String? = "text", // "image", "video", "file", "text"
    val isSentByMe: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)