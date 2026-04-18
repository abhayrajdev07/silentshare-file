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

    // 🔥 NEW STATE: For Session Type Selection (Individual/Group)
    private var goToSessionTypeSelection by mutableStateOf(false)

    private var goToConnectionSetup by mutableStateOf(false)
    private var goToMessaging by mutableStateOf(false)

    private var userName by mutableStateOf("")
    private var userAvatar by mutableStateOf<Uri?>(null)

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
                            onBack = { goToMessaging = false }
                        )
                    }

                    // 🔥 2. CONNECTION SCREEN
                    goToConnectionSetup -> {
                        ConnectionSetupKtorScreen(
                            userName = userName,
                            avatarUri = userAvatar,

                            onCreateServer = {
                                // 🛠️ FIX: Server ab pehle hi start ho chuka hai, yahan se bas chat me jao
                                goToConnectionSetup = false
                                goToMessaging = true
                            },

                            onJoinServer = { ip ->
                                isServer = false
                                serverIp = ip
                                goToConnectionSetup = false
                                goToMessaging = true
                            },

                            onBack = {
                                // 🛠️ FIX: Agar host back dabaye, toh server band kar do
                                if (isServer) {
                                    LocalServer.stopServer()
                                }
                                goToConnectionSetup = false
                            }
                        )
                    }

                    // 🔥 3. CREATE SESSION SCREEN (Individual vs Group)
                    goToSessionTypeSelection -> {
                        CreateSessionScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onIndividualChat = {
                                // 🚀 THE MAGIC FIX: Jaise hi Host banne ka decide kiya, Server YAHIN start kar do!
                                isServer = true
                                serverIp = ""
                                LocalServer.startServer() // 🔥 Server starts BEFORE showing QR code

                                goToSessionTypeSelection = false
                                goToConnectionSetup = true
                            },
                            onGroupChat = {
                                isServer = true
                                serverIp = ""
                                LocalServer.startServer()

                                goToSessionTypeSelection = false
                                goToConnectionSetup = true
                            },
                            onBack = {
                                goToSessionTypeSelection = false
                            }
                        )
                    }

                    // 🔥 4. PROFILE
                    !isProfileCreated -> {
                        ProfileScreen { name, imageUri ->
                            userName = name
                            userAvatar = imageUri
                            isProfileCreated = true
                        }
                    }

                    // 🔥 5. HOME
                    else -> {
                        HomeScreen(
                            userName = userName,
                            avatarUri = userAvatar,

                            onCreateSession = {
                                // 🔥 CHANGED: Ab sidha connection pe nahi, pehle 'Type Selection' screen par jayega
                                goToSessionTypeSelection = true
                            },

                            onJoinSession = {
                                goToConnectionSetup = true
                            },

                            onBack = {}
                        )
                    }
                }
            }
        }
    }
}