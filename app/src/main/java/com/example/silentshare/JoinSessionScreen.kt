package com.example.silentshare

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

// ── Color Palette ──────────────────────────────────────────────────────────────
private val BgDark = Color(0xFF0A0E1A)
private val Surface1 = Color(0xFF111827)
private val Surface2 = Color(0xFF1C2438)
private val AccentCyan = Color(0xFF00D4FF)
private val AccentBlue = Color(0xFF3B82F6)
private val AccentPurple = Color(0xFF8B5CF6)
private val TextPrimary = Color(0xFFF1F5F9)
private val TextMuted = Color(0xFF64748B)
private val ScanGlow = Color(0xFF00D4FF).copy(alpha = 0.25f)
private val DividerLine = Color(0xFF1E293B)

val SessionGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF0A0E1A), Color(0xFF0F172A), Color(0xFF0A0E1A))
)
val GlowBrush = Brush.radialGradient(
    colors = listOf(Color(0xFF00D4FF).copy(alpha = 0.18f), Color.Transparent),
    radius = 420f
)

@Composable
fun JoinSessionScreen(
    userName: String,
    avatarUri: Uri?,
    onJoinServer: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val gatewayIp = remember { NetworkUtils.getGatewayIp(context) }
    var ipInput by remember { mutableStateOf(gatewayIp) }
    var ipFieldFocused by remember { mutableStateOf(false) }
    var scanning by remember { mutableStateOf(false) }

    // Pulse animation for QR button
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = EaseInOutSine), RepeatMode.Reverse
        ), label = "scale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f, targetValue = 0.40f,
        animationSpec = infiniteRepeatable(
            tween(1100, easing = EaseInOutSine), RepeatMode.Reverse
        ), label = "glow"
    )

    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        scanning = false
        if (result.contents != null) onJoinServer(result.contents)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        // Background ambient glow
        Box(
            modifier = Modifier
                .size(600.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-80).dp)
                .background(GlowBrush)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Top Bar ──────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Surface2)
                ) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // User info chip
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Surface2)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Avatar circle
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AccentCyan, AccentPurple)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = userName,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Heading ──────────────────────────────────────────────────
            Text(
                text = "Join Session",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Connect via QR code or enter IP manually",
                fontSize = 14.sp,
                color = TextMuted,
                letterSpacing = 0.1.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // ── QR Scan Card ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(pulseScale)
                    .drawBehind {
                        drawCircle(
                            color = AccentCyan.copy(alpha = glowAlpha),
                            radius = size.minDimension * 0.62f,
                            center = center
                        )
                    }
                    .clip(RoundedCornerShape(28.dp))
                    .background(Surface2)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(AccentCyan.copy(alpha = 0.6f), AccentPurple.copy(alpha = 0.4f))
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clickable {
                        scanning = true
                        val options = ScanOptions().apply {
                            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            setPrompt("Scan the Host's QR Code")
                            setCameraId(0)
                            setBeepEnabled(true)
                            setBarcodeImageEnabled(true)
                        }
                        barcodeLauncher.launch(options)
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // QR Icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(AccentCyan.copy(0.25f), Color.Transparent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CameraAlt,
                            contentDescription = null,
                            tint = AccentCyan,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        "Scan QR Code",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.2.sp
                    )
                    Text(
                        "Tap to open scanner",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                // Corner accents
                CornerAccents()
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ── Divider ───────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = DividerLine
                )
                Text(
                    "or connect manually",
                    color = TextMuted,
                    fontSize = 12.sp,
                    letterSpacing = 0.4.sp
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = DividerLine
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── IP Input Card ─────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Surface1)
                    .border(
                        width = 1.dp,
                        color = if (ipFieldFocused) AccentBlue.copy(0.5f) else DividerLine,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AccentBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.Wifi,
                            contentDescription = null,
                            tint = AccentBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        "Server IP Address",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = ipInput,
                    onValueChange = { ipInput = it },
                    placeholder = {
                        Text("e.g. 192.168.1.100", color = TextMuted, fontSize = 14.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { ipFieldFocused = it.isFocused },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onJoinServer(ipInput)
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AccentBlue,
                        unfocusedBorderColor = Surface2,
                        cursorColor = AccentCyan,
                        focusedContainerColor = Surface2,
                        unfocusedContainerColor = Surface2,
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Join Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onJoinServer(ipInput)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    enabled = ipInput.isNotBlank()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(AccentBlue, AccentPurple)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Connect",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Status hint
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Surface1)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(AccentCyan)
                )
                Text(
                    "Make sure you're on the same Wi-Fi network",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Corner accent lines drawn at each corner of the QR scan box ───────────────
@Composable
private fun CornerAccents() {
    val stroke = 2.5.dp
    val len = 18.dp
    val pad = 10.dp
    val color = AccentCyan

    // Top-left
    Box(modifier = Modifier.fillMaxSize()) {
        // TL horizontal
        Box(Modifier
            .size(len, stroke)
            .offset(pad, pad)
            .background(color)
            .align(Alignment.TopStart))
        Box(Modifier
            .size(stroke, len)
            .offset(pad, pad)
            .background(color)
            .align(Alignment.TopStart))
        // TR
        Box(Modifier
            .size(len, stroke)
            .offset(-pad, pad)
            .align(Alignment.TopEnd)
            .background(color))
        Box(Modifier
            .size(stroke, len)
            .offset(-pad, pad)
            .align(Alignment.TopEnd)
            .background(color))
        // BL
        Box(
            Modifier
                .size(len, stroke)
                .offset(pad, -pad)
                .align(Alignment.BottomStart)
                .background(color)
        )
        Box(
            Modifier
                .size(stroke, len)
                .offset(pad, -pad)
                .align(Alignment.BottomStart)
                .background(color)
        )
        // BR
        Box(
            Modifier
                .size(len, stroke)
                .offset(-pad, -pad)
                .align(Alignment.BottomEnd)
                .background(color)
        )
        Box(
            Modifier
                .size(stroke, len)
                .offset(-pad, -pad)
                .align(Alignment.BottomEnd)
                .background(color)
        )
    }
}