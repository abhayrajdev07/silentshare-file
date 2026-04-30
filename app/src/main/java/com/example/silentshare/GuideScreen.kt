package com.example.silentshare

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentshare.ui.theme.SilentShareTheme

// ── Shared Modern Palette ─────────────────────────────────────────────────────
private val BgDark = Color(0xFF0A0E1A)
private val Surface1 = Color(0xFF111827)
private val Surface2 = Color(0xFF1C2438)
private val AccentCyan = Color(0xFF00D4FF)
private val AccentBlue = Color(0xFF3B82F6)
private val TextPrimary = Color(0xFFF1F5F9)
private val TextMuted = Color(0xFF94A3B8)

class GuideActivityNow : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Modern Edge-to-Edge handling
        enableEdgeToEdge()

        setContent {
            SilentShareTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent
                ) { innerPadding ->
                    GuideScreen(
                        innerPadding = innerPadding,
                        onClose = {
                            // Safely start AfterSplash and finish this activity
                            startActivity(Intent(this@GuideActivityNow, AfterSplash::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GuideScreen(
    innerPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // 🔹 Modern Gradient Background matching the rest of the app
            .background(Brush.verticalGradient(listOf(Surface1, BgDark)))
            .padding(innerPadding)
    ) {
        // 1️⃣ SCROLLABLE CONTENT (Placed first to be the bottom layer)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 80.dp) // Added extra top padding so header doesn't hide under the button
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {

            // 🔹 HEADER
            Text(
                text = "Documentation",
                style = TextStyle(
                    brush = Brush.linearGradient(listOf(AccentCyan, AccentBlue)),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Everything you need to know about SilentShare.",
                color = TextMuted,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 DOC SECTIONS
            GuideSection(
                title = "SilentShare",
                icon = Icons.Default.MenuBook,
                points = listOf(
                    "Secure offline messaging",
                    "Works without internet",
                    "Fast peer-to-peer connection",
                    "Simple and easy to use"
                )
            )

            GuideSection(
                title = "Privacy First",
                icon = Icons.Default.Security,
                points = listOf(
                    "No data tracking",
                    "No cloud storage",
                    "End-to-end secure transfer",
                    "Your data stays on your device"
                )
            )

            GuideSection(
                title = "Offline Messaging",
                icon = Icons.Default.WifiOff,
                points = listOf(
                    "No internet required",
                    "Uses WiFi Direct internally",
                    "Instant device connection",
                    "Perfect for emergencies"
                )
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        // 2️⃣ CLOSE BUTTON (Placed last to be the top layer)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 24.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Surface2.copy(alpha = 0.8f)) // Increased opacity for better visibility
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                .clickable {
                    onClose() // 🔥 Now this will trigger properly
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun GuideSection(title: String, icon: ImageVector, points: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface2.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Card Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AccentBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bullet Points
            points.forEach { point ->
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Bullet",
                        tint = AccentCyan.copy(alpha = 0.8f),
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = point,
                        fontSize = 15.sp,
                        color = TextMuted,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}