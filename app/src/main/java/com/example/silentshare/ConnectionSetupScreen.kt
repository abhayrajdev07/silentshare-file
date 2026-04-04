package com.example.silentshare

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.silentshare.ui.theme.BlueGradient


@Composable
fun ConnectionSetupScreen(
    userName: String,
    avatarUri: Uri?,
    onBack: () -> Unit,
    onConnected: () -> Unit
) {

    var generatedCode by remember { mutableStateOf("") }

    fun generateCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding(), // 🔥 THIS FIXES SYSTEM BAR VISIBILITY
        verticalArrangement = Arrangement.Top
    ) {

        // 🔥 TOP BAR (STICKS TO TOP)
        TopBar(
            userName = userName,
            avatarUri = avatarUri,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 40.dp)

        ) {


            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Connection Setup",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 QR OPTION
            Button(
                onClick = {
                    // TODO: Generate QR
                    onConnected()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("QR Based Connection")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 CODE OPTION
            Button(
                onClick = {
                    generatedCode = generateCode()
                    onConnected()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Code Based Connection")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 SHOW CODE
            if (generatedCode.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1F2C34), RoundedCornerShape(10.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = generatedCode,
                        color = Color.Green,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}