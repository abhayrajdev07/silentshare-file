package com.example.silentshare

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.silentshare.ui.theme.BlueGradient

// ── Color tokens ──────────────────────────────────────────────────────────────
private val SheetBg = Color(0xFF0F0F12)
private val SheetSurface = Color(0xFF1A1A22)
private val AccentBlue = Color(0xFF4F8EF7)
private val AccentPurple = Color(0xFF9B6FF7)
private val TextPrimary = Color(0xFFF2F2F7)
private val TextSecondary = Color(0xFF8A8A9A)
private val DividerColor = Color(0xFF2C2C38)
private val RingColor = Color(0xFF2E2E40)

private val ButtonGradient = Brush.horizontalGradient(
    listOf(AccentBlue, AccentPurple)
)

@Composable
fun GroupSetupScreen(
    userName: String,
    avatarUri: Uri?,
    onCreateGroup: (String, Uri?) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var groupNameInput by remember { mutableStateOf("") }
    var groupImageUri by remember { mutableStateOf<Uri?>(null) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) groupImageUri = uri }

    // ── Root: blurred/dimmed background ──────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {


        // ── Bottom Sheet Card ─────────────────────────────────────────────────
        AnimatedVisibility(
            visible = visible,


            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 32.dp,
                        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                        ambientColor = AccentBlue.copy(alpha = 0.18f),
                        spotColor = AccentPurple.copy(alpha = 0.18f)
                    )
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(SheetBg),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Drag handle
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(DividerColor)
                )
                Spacer(Modifier.height(28.dp))

                // ── Header ────────────────────────────────────────────────────
                Text(
                    text = "New Group",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = (-0.5).sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Give your group a name and a face",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(Modifier.height(32.dp))

                // ── Avatar picker ─────────────────────────────────────────────
                Box(contentAlignment = Alignment.BottomEnd) {
                    // Outer glow ring
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                brush = Brush.sweepGradient(
                                    listOf(AccentBlue, AccentPurple, AccentBlue)
                                ),
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(SheetSurface)
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (groupImageUri != null) {
                            AsyncImage(
                                model = groupImageUri,
                                contentDescription = "Group icon",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Pick image",
                                tint = TextSecondary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // Edit badge
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(listOf(AccentBlue, AccentPurple))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (groupImageUri == null) "Add group photo" else "Change photo",
                    fontSize = 12.sp,
                    color = AccentBlue,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(28.dp))

                // ── Divider ───────────────────────────────────────────────────
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = DividerColor,
                    thickness = 1.dp
                )

                Spacer(Modifier.height(28.dp))

                // ── Text field ────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .imePadding()
                ) {
                    Text(
                        text = "GROUP NAME",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = groupNameInput,
                        onValueChange = { groupNameInput = it },
                        placeholder = {
                            Text(
                                "e.g. Cyber Squad",
                                color = TextSecondary,
                                fontSize = 15.sp
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = SheetSurface,
                            focusedContainerColor = SheetSurface,
                            unfocusedBorderColor = DividerColor,
                            focusedBorderColor = AccentBlue,
                            cursorColor = AccentBlue,
                            unfocusedTextColor = TextPrimary,
                            focusedTextColor = TextPrimary
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        trailingIcon = {
                            if (groupNameInput.isNotBlank()) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = AccentBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    )
                    Spacer(Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp)
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .then(
                                if (groupNameInput.isNotBlank())
                                    Modifier.background(ButtonGradient)
                                else
                                    Modifier.background(SheetSurface)
                            )
                            .clickable(enabled = groupNameInput.isNotBlank()) {
                                onCreateGroup(groupNameInput, groupImageUri)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Create Group",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (groupNameInput.isNotBlank()) Color.White
                            else TextSecondary,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.3.sp
                        )
                    }
                    // Nav-bar padding
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                    Spacer(Modifier.height(16.dp))

                }


            }
        }
    }
}