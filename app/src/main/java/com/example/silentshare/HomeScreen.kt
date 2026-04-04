package com.example.silentshare

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.silentshare.ui.theme.BlueGradient

@Composable
fun HomeScreen(
    userName: String,
    avatarUri: Uri?,
    onCreateSession: () -> Unit,
    onJoinSession: () -> Unit,
    onBack: () -> Unit
) {

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

        // 🔹 CONTENT AREA (CENTERED)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = onCreateSession,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 20.dp), // 🔥 adds space left & right

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,      // 🔥 button background
                        contentColor = Color.White         // 🔥 text color
                    )
                ) {
                    Text("Create Session")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onJoinSession,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp) // 🔥 adds space left & right

                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,      // 🔥 button background
                        contentColor = Color.White         // 🔥 text color
                    )
                ) {
                    Text("Join Session")
                }
            }
        }
    }
}