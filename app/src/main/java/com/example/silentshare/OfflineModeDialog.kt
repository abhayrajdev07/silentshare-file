package com.example.silentshare

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MobileOff
import androidx.compose.material.icons.outlined.PhonelinkLock
import androidx.compose.material.icons.outlined.SignalWifiOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch

// ── Tokens ─────────────────────────────────────────────────────────────────────
private val BgCard = Color(0xFF141920)   // rich dark navy — no transparency
private val BgStep = Color(0xFF1C2330)   // slightly lighter for step rows
private val BgStepIcon = Color(0xFF232D3E)   // icon circle fill
private val RimColor = Color(0xFF2A3447)   // card border

private val AccentGreen = Color(0xFF25D97F)
private val AccentTeal = Color(0xFF00C9B1)

private val TextPrimary = Color(0xFFF1F5FF)
private val TextSecondary = Color(0xFF8994AB)
private val TextDim = Color(0xFF4E5A70)

// ── Step row ───────────────────────────────────────────────────────────────────
@Composable
private fun StepRow(icon: ImageVector, label: String, sub: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BgStep)
            .border(1.dp, RimColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BgStepIcon)
                .border(
                    1.dp,
                    AccentGreen.copy(alpha = 0.20f),
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentGreen,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(sub, fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)
        }
    }
}

// ── Dialog ─────────────────────────────────────────────────────────────────────
@Composable
fun OfflineModeDialog(
    onDismiss: () -> Unit,
    onDontShowAgain: () -> Unit
) {
    val scale = remember { Animatable(0.92f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { alpha.animateTo(1f, tween(180)) }
        scale.animateTo(
            1f,
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .scale(scale.value)
                .alpha(alpha.value)
                .clip(RoundedCornerShape(24.dp))
                .background(BgCard)
                .border(1.dp, RimColor, RoundedCornerShape(24.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(AccentGreen.copy(alpha = 0.18f), AccentTeal.copy(alpha = 0.10f))
                        )
                    )
                    .border(1.dp, AccentGreen.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhonelinkLock,
                    contentDescription = null,
                    tint = AccentGreen,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Offline Mode Required",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.3).sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Complete both steps to connect securely",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(Modifier.height(16.dp))

            // Step 1
            StepRow(
                icon = Icons.Outlined.MobileOff,
                label = "Disable Mobile Data",
                sub = "Turn off cellular data on both devices"
            )

            Spacer(Modifier.height(8.dp))

            // Step 2
            StepRow(
                icon = Icons.Outlined.SignalWifiOff,
                label = "Join Same Wi-Fi",
                sub = "Connect both to the same network or hotspot"
            )

            Spacer(Modifier.height(20.dp))

            HorizontalDivider(color = RimColor, thickness = 0.5.dp)

            Spacer(Modifier.height(16.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Secondary
                OutlinedButton(
                    onClick = { onDontShowAgain(); onDismiss() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RimColor),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDim)
                ) {
                    Text("Don't show", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                // Primary
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(listOf(AccentGreen, AccentTeal))
                        )
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF081410))
                    ) {
                        Text("Got it", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}