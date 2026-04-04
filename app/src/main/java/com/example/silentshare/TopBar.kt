package com.example.silentshare

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    userName: String,
    avatarUri: Uri?,
    onBack: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // 🔙 LEFT SIDE (Back + Avatar + Name)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            BackButton(onClick = onBack)

            Spacer(modifier = Modifier.width(8.dp))

            // 👤 Avatar FIRST
            if (avatarUri != null) {
                coil.compose.AsyncImage(
                    model = avatarUri,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(40.dp) // 🔥 fixed size
                        .clip(CircleShape), // 🔥 makes perfect circle
                    contentScale = ContentScale.Crop // 🔥 fills properly (IMPORTANT)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 🧑 Username AFTER avatar
            Text(
                text = userName,
                color = Color.White
            )
        }

        // 🔹 Right side (empty or future actions)
        Spacer(modifier = Modifier.width(1.dp))
    }
}