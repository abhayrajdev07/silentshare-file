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
fun CreateSessionScreen(
    userName: String,
    avatarUri: Uri?,
    onIndividualChat: () -> Unit,
    onGroupChat: () -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding(), // 🔥 Status bar overlap fix
        verticalArrangement = Arrangement.Top
    ) {

        // 🔥 TOP BAR (Stays at the top)
        TopBar(
            userName = userName,
            avatarUri = avatarUri,
            onBack = onBack
        )

        // 🔹 CONTENT AREA (Centered perfectly)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 🔥 INDIVIDUAL CHAT BUTTON
                Button(
                    onClick = onIndividualChat,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Individual Chat")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 🔥 GROUP CHAT BUTTON
                Button(
                    onClick = onGroupChat,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Group Chat")
                }
            }
        }
    }
}