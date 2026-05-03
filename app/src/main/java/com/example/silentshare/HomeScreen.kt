package com.example.silentshare

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.silentshare.ui.theme.BlueGradient


@Composable
fun HomeScreen(
    userName: String,
    avatarUri: Uri?,
    onCreateSession: () -> Unit,
    onJoinSession: () -> Unit,
    onBack: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showDialog = true
    }

    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BlueGradient)
                .blur(if (showDialog) 52.dp else 0.dp) // 🔥 BLUR HERE
                .alpha(if (showDialog) 0.5f else 1f)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 🔥 YOUR EXISTING UI (NO CHANGE)
        }

        // 🔥 POPUP
        if (showDialog) {
            OfflineModeDialog(
                onDismiss = { showDialog = false },
                onDontShowAgain = { showDialog = false }
            )
        }
    }
    // Intercepts the system back gesture
    BackHandler {
        onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // 🔥 MODERN HEADER (Greeting Left, Avatar Right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side: Personalized Greeting
            Column {
                Text(
                    text = "Hi, $userName \uD83D\uDC4B", // Waving hand emoji for a friendly touch
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ready to share?",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

            // Right Side: Profile Picture
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)), // Subtle frosted ring
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri != null) {
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(48.dp) // Slightly smaller than the box to show the ring
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Profile",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // 🔹 HERO SECTION (Centered graphic & Welcome text)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Takes up remaining space
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Modern circular icon background with a soft semi-transparent overlay
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WifiTethering,
                        contentDescription = "Share Icon",
                        tint = Color(0xFF005C97), // Deep blue tint to match gradient vibe
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SilentShare",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fast, secure, and offline sharing.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
        }

        // 🔥 BOTTOM ACTION CARD
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Get Started",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // CREATE SESSION BUTTON
                Button(
                    onClick = onCreateSession,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AddModerator,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Create Session", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // JOIN SESSION BUTTON
                Button(
                    onClick = onJoinSession,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF0F0F0),
                        contentColor = Color.Black
//                        containerColor = Color.Green,
//                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Join Session", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}