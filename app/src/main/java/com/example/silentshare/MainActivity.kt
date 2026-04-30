package com.example.silentshare

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.silentshare.ui.theme.SilentShareTheme

class MainActivity : ComponentActivity() {

    private var isProfileCreated by mutableStateOf(false)

    // 🔥 NAVIGATION STATES
    private var goToSessionType by mutableStateOf(false)   // Selection Screen (Individual/Group)
    private var goToGroupSetup by mutableStateOf(false)    // NEW: Group Name & Avatar Screen
    private var goToCreateSession by mutableStateOf(false) // QR Code Screen (Host)
    private var goToJoinSession by mutableStateOf(false)   // Scanner Screen (Client)
    private var goToMessaging by mutableStateOf(false)     // Chat

    private var userName by mutableStateOf("")
    private var userAvatar by mutableStateOf<Uri?>(null)

    // 🔥 GROUP DETAILS STATE (To pass into chat later if needed)
    private var currentGroupName by mutableStateOf("")
    private var currentGroupAvatar by mutableStateOf<Uri?>(null)

    // 🔥 KTOR STATE
    private var isServer by mutableStateOf(false)
    private var serverIp by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SilentShareTheme {

                when {

                    // 🔥 1. CHAT SCREEN
                    goToMessaging -> {
                        MessagingScreenKtor(
                            userName = userName,
                            avatarUri = userAvatar,
                            serverIp = serverIp,
                            isServer = isServer,
                            onBack = {
                                goToMessaging = false
                            }
                        )
                    }

                    // 🔥 2. QR CODE SCREEN (Host)
                    goToCreateSession -> {
                        CreateSessionScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onCreateServer = {
                                goToCreateSession = false
                                goToMessaging = true
                            },
                            onBack = {
                                // Stop server and go back
                                LocalServer.stopServer()
                                goToCreateSession = false
                                goToSessionType = true
                            }
                        )
                    }

                    // 🔥 3. GROUP SETUP SCREEN (Create Name & Photo)
                    goToGroupSetup -> {
                        GroupSetupScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onCreateGroup = { name, uri ->
                                currentGroupName = name
                                currentGroupAvatar = uri

                                // Start Server NOW that group info is filled
                                isServer = true
                                serverIp = ""
                                LocalServer.startServer()

                                goToGroupSetup = false
                                goToCreateSession = true // Move to QR Code Screen
                            },
                            onBack = {
                                goToGroupSetup = false
                                goToSessionType = true
                            }
                        )
                    }

                    // 🔥 4. SESSION TYPE SELECTION (Individual/Group)
                    goToSessionType -> {
                        SessionTypeScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onIndividualChat = {
                                // Individual Chat goes straight to QR Code
                                isServer = true
                                serverIp = ""
                                LocalServer.startServer()

                                goToSessionType = false
                                goToCreateSession = true
                            },
                            onGroupChat = {
                                // Group Chat routes to Group Setup Screen FIRST
                                goToSessionType = false
                                goToGroupSetup = true
                            },
                            onBack = {
                                goToSessionType = false
                            }
                        )
                    }

                    // 🔥 5. JOIN SESSION SCREEN (CLIENT)
                    goToJoinSession -> {
                        JoinSessionScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onJoinServer = { ip ->
                                isServer = false
                                serverIp = ip
                                goToJoinSession = false
                                goToMessaging = true
                            },
                            onBack = {
                                goToJoinSession = false
                            }
                        )
                    }

                    // 🔥 6. PROFILE
                    !isProfileCreated -> {
                        ProfileScreen { name, imageUri ->
                            userName = name
                            userAvatar = imageUri
                            isProfileCreated = true
                        }
                    }

                    // 🔥 7. HOME
                    else -> {
                        HomeScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onCreateSession = {
                                goToSessionType = true
                            },
                            onJoinSession = {
                                goToJoinSession = true
                            },
                            onBack = {}
                        )
                    }
                }
            }
        }
    }
}