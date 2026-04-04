package com.example.silentshare

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.silentshare.ui.theme.BlueGradient

@Composable
fun CreateSessionScreen(
    userName: String,
    avatarUri: Uri?,
    onIndividualChat: () -> Unit,
    onGroupChat: () -> Unit,
    onBroadcastChat: () -> Unit,
    onBack: () -> Unit // ✅ ADD THIS

) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .padding(0.dp)
            .statusBarsPadding(), // 🔥 THIS FIXES SYSTEM BAR VISIBILITY
        verticalArrangement = Arrangement.Top
    ) {

        // 🔥 TOP BAR (STICKS TO TOP)
        TopBar(
            userName = userName,
            avatarUri = avatarUri,
            onBack = onBack
        )

//
        Spacer(modifier = Modifier.weight(1f))

        // 🔹 Center Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Button(
                onClick = onIndividualChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 20.dp), // 🔥 adds space left & right

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,      // 🔥 button background
                    contentColor = Color.White         // 🔥 text color
                )
            ) {
                Text("Individual Chat")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onGroupChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 20.dp), // 🔥 adds space left & right
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,      // 🔥 button background
                    contentColor = Color.White         // 🔥 text color
                )
            ) {
                Text("Group Chat")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onBroadcastChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 20.dp), // 🔥 adds space left & right

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,      // 🔥 button background
                    contentColor = Color.White         // 🔥 text color
                )
            ) {
                Text("Broadcast Chat")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}