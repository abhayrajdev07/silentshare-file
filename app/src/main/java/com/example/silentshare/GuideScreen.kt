package com.example.silentshare

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentshare.ui.theme.BlueGradient
import android.graphics.Color as AndroidColor
import androidx.core.view.WindowInsetsControllerCompat


class GuideActivityNow : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 EDGE TO EDGE
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 🔥 REMOVE ALL COLORS
        window.statusBarColor = AndroidColor.TRANSPARENT
        window.navigationBarColor = AndroidColor.TRANSPARENT

        // 🔥 REMOVE GRAY SCRIM (VERY IMPORTANT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }

        // 🔥 ICON COLOR (white icons)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        setContent {
            GuideScreen()
        }
    }
}

@Composable
fun GuideScreen() {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
    ) {

        // ❌ CLOSE BUTTON (TOP RIGHT)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 20.dp)
                .size(44.dp) // 🔥 circle size
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f)) // 🔥 subtle background
                .border(
                    width = 1.5.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clickable {
                    context.startActivity(Intent(context, AfterSplash::class.java))
                    (context as? ComponentActivity)?.finish()
                },
            contentAlignment = Alignment.Center // 🔥 centers icon perfectly
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier.size(20.dp) // 🔥 icon size
            )
        }

        // 🔹 SCROLLABLE CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GuideSection(
                "SilentShare",
                listOf(
                    "Secure offline messaging",
                    "Works without internet",
                    "Fast peer-to-peer connection",
                    "Simple and easy to use"
                )
            )

            GuideSection(
                "Privacy First",
                listOf(
                    "No data tracking",
                    "No cloud storage",
                    "End-to-end secure transfer",
                    "Your data stays on your device"
                )
            )

            GuideSection(
                "Offline Messaging",
                listOf(
                    "No internet required",
                    "Uses WiFi Direct",
                    "Instant device connection",
                    "Perfect for emergencies"
                )
            )

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
            .border(
                width = 0.3.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(20.dp)


            )
            .background(
                color = Color.White.copy(alpha = 0.09f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)

    ) {

        Column {

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(15.dp))

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