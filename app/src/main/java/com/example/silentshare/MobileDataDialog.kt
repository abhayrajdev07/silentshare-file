package com.example.silentshare

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MobileDataWarningDialog(onExit: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onExit) {
                Text("Exit App")
            }
        },
        title = {
            Text("⚠ Mobile Data Detected")
        },
        text = {
            Text("Please turn OFF mobile data to use SilentShare.\n\nThis app works only in offline mode for maximum security.")
        }
    )
}