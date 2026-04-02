package com.example.silentshare

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun GuideScreen() {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
    ) {

        // ❌ CLOSE BUTTON (TOP RIGHT)
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 20.dp)
                .size(28.dp)
                .clickable {
                    context.startActivity(Intent(context, AfterSplash::class.java))
                    (context as? ComponentActivity)?.finish()
                }
        )

        // 🔹 CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Section 1
            GuideSection(
                "SilentShare",
                listOf(
                    "Secure offline messaging",
                    "Works without internet",
                    "Fast peer-to-peer connection",
                    "Simple and easy to use"
                )
            )

//            Divider(color = Color.Gray, thickness = 1.dp)

            // Section 2
            GuideSection(
                "Privacy First",
                listOf(
                    "No data tracking",
                    "No cloud storage",
                    "End-to-end secure transfer",
                    "Your data stays on your device"
                )
            )

//            Divider(color = Color.Gray, thickness = 1.dp)

            // Section 3
            GuideSection(
                "Offline Messaging",
                listOf(
                    "No internet required",
                    "Uses WiFi Direct",
                    "Instant device connection",
                    "Perfect for emergencies"
                )
            )
//            Divider(color = Color.Gray, thickness = 1.dp)


            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
@Composable
fun GuideSection(title: String, points: List<String>) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {

        Column {

            // 🔹 TITLE (CENTER)
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),

            )

            Spacer(modifier = Modifier.height(15.dp))

            // 🔹 POINTS (LEFT SIDE)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                points.forEach { point ->
                    Text(
                        text = "• $point",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}