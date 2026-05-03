package com.example.silentshare

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentshare.ui.theme.SilentShareTheme
import com.example.silentshare.ui.theme.White

// ── Palette ───────────────────────────────────────────────────────────────────
private val BgDark = Color(0xFF0A0E1A)
private val Surface1 = Color(0xFF111827)
private val Surface2 = Color(0xFF1C2438)
private val AccentCyan = Color(0xFF00D4FF)
private val AccentBlue = Color(0xFF3B82F6)
private val AccentPurple = Color(0xFF8B5CF6)
private val TextPrimary = Color(0xFFF1F5F9)
private val TextMuted = Color(0xFF94A3B8)

class AfterSplash : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SilentShareTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AfterSplashScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun AfterSplashScreen(innerPadding: PaddingValues = PaddingValues()) {

    val context = LocalContext.current
    val activity = context as? ComponentActivity

    Box(
        modifier = Modifier
            .fillMaxSize()
            // 🔹 Modern Gradient Background
            .background(Brush.verticalGradient(listOf(Surface1, BgDark)))
//            .background(Color.Black)
            .padding(innerPadding)
    ) {

        // 🔹 HELP ICON (Modern Glassmorphic Style)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(White.copy(alpha = 1f))
                    .clickable {
                        context.startActivity(Intent(context, GuideActivity::class.java))
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bookhelp),
                    contentDescription = "Docs",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Guide",
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // 🔹 MAIN CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp) // Added horizontal padding for breathing room
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Beautifully styled logo with a soft shadow
            Image(
                painter = painterResource(id = R.drawable.logoss1),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(160.dp)
                    .shadow(16.dp, RoundedCornerShape(36.dp))
                    .background(Color.White, shape = RoundedCornerShape(36.dp)),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(50.dp))

            // 🔹 HERO TEXT (Tighter spacing, gradient highlight)
            Text(
                text = "India's First",
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Privacy Focused",
                style = TextStyle(
                    brush = Brush.linearGradient(listOf(AccentCyan, AccentBlue)),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Text(
                text = "Offline Messenger",
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Connect, chat, and share files locally\nwithout relying on the internet.",
                color = TextMuted,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

            // ── Get Started button ────────────────────────────────────────
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                    activity?.finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 32.dp), // Pushes button slightly up from the very bottom
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(AccentBlue, AccentPurple)),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Get Started",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            "→",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}