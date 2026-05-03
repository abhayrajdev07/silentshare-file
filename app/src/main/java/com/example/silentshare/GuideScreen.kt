package com.example.silentshare

import android.app.Activity
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WifiOff
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

        enableEdgeToEdge()

        setContent {
            SilentShareTheme {
                // 🔥 IMPORTANT: CALL AFTER SPLASH SCREEN HERE
                AfterSplashScreen()
            }
        }
    }
}

@Composable
fun AfterSplashScreen() {

    val context = androidx.compose.ui.platform.LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // 🔥 CONTINUOUS CHECK (SMOOTH + LIGHT)
    LaunchedEffect(Unit) {
        while (true) {
            showDialog = isMobileDataOn(context)
            kotlinx.coroutines.delay(1000)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔥 YOUR MAIN APP UI
        MainScreenUI()

        // 🔥 POPUP (ALWAYS ON TOP)
        if (showDialog) {
            MobileDataWarningDialog(
                onExit = {
                    (context as? Activity)?.finishAffinity()
                }
            )
        }
    }
}

@Composable
fun MainScreenUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "SilentShare Home",
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
fun GuideScreen(
    innerPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit
) {

    // ✅ ONLY SCROLL STATE (lightweight)
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Surface1, BgDark)))
            .padding(innerPadding)
    ) {

        // 🔥 FIXED HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Text(
                text = "How SilentShare Works",
                style = TextStyle(
                    brush = Brush.linearGradient(listOf(AccentCyan, AccentBlue)),
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fast, secure and completely offline \nfile sharing experience.",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        // 🔥 SCROLLABLE CONTENT (SMOOTH NOW)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            GuideSection(
                title = "What is SilentShare",
                icon = Icons.Default.MenuBook,
                points = listOf(
                    "SilentShare enables instant file and message transfer without internet",
                    "Built for speed, privacy, and real-world offline usage",
                    "Works seamlessly between nearby devices",
                    "No login, no cloud, no delay"
                )
            )

            GuideSection(
                title = "How to Use",
                icon = Icons.Default.CheckCircle,
                points = listOf(
                    "Open SilentShare on both devices",
                    "Ensure WiFi or wireless connection is enabled",
                    "Select the file or message you want to send",
                    "Connect to the nearby device instantly",
                    "Transfer completes securely within seconds"
                )
            )

            GuideSection(
                title = "Privacy & Security",
                icon = Icons.Default.Security,
                points = listOf(
                    "No internet means zero tracking or data leaks",
                    "End-to-end secure peer-to-peer transfer",
                    "No cloud storage or third-party servers involved",
                    "Your data always stays on your device"
                )
            )

            GuideSection(
                title = "Offline Technology",
                icon = Icons.Default.WifiOff,
                points = listOf(
                    "Uses advanced peer-to-peer connectivity",
                    "Works even in zero network environments",
                    "Instant device discovery and connection",
                    "Ideal for emergencies and remote areas"
                )
            )

            GuideSection(
                title = "Requirements",
                icon = Icons.Default.CheckCircle,
                points = listOf(
                    "Both devices must have SilentShare installed",
                    "Devices should be within nearby range",
                    "WiFi or wireless connectivity must be enabled",
                    "Permissions for file access may be required"
                )
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        // 🔥 CLOSE BUTTON
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 24.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Surface2.copy(alpha = 0.8f))
                .border(1.dp, Color.White.copy(alpha = 0.9f), CircleShape)
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
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