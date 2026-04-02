package com.example.silentshare

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import com.example.silentshare.ui.theme.SilentShareTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SilentShareTheme {

                // 🔹 STATES
                var isProfileCreated by remember { mutableStateOf(false) }
                var goToCreateSession by remember { mutableStateOf(false) }
                var goToMessaging by remember { mutableStateOf(false) }

                var userName by remember { mutableStateOf("") }
                var userAvatar by remember { mutableStateOf<Uri?>(null) }

                // 🔹 PERMISSION HANDLER
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val granted = permissions.values.all { it }
                    if (granted) {
                        goToMessaging = true
                    }
                }

                // 🔹 NAVIGATION FLOW
                when {

                    // 1️⃣ PROFILE
                    !isProfileCreated -> {
                        ProfileScreen { name, imageUri ->
                            userName = name
                            userAvatar = imageUri
                            isProfileCreated = true
                        }
                    }

                    // 2️⃣ CREATE SESSION
                    goToCreateSession -> {
                        CreateSessionScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onIndividualChat = { goToMessaging = true },
                            onGroupChat = { goToMessaging = true },
                            onBroadcastChat = { goToMessaging = true },

                            // 🔙 BACK
                            onBack = {
                                goToCreateSession = false
                            }
                        )
                    }

                    // 3️⃣ MESSAGING
                    goToMessaging -> {
                        MessagingScreen(
                            userName = userName,

                            // 🔙 BACK
                            onBack = {
                                goToMessaging = false
                            }
                        )
                    }

                    // 4️⃣ HOME
                    else -> {
                        HomeScreen(
                            userName = userName,
                            avatarUri = userAvatar,

                            onCreateSession = {
                                goToCreateSession = true
                            },

                            onJoinSession = {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.NEARBY_WIFI_DEVICES
                                    )
                                )
                            },

                            onBack = {
                                // optional (home usually doesn't need back)
                            }
                        )
                    }
                }
            }
        }
    }
}