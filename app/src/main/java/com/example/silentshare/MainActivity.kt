package com.example.silentshare

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.silentshare.ui.theme.SilentShareTheme

class MainActivity : ComponentActivity() {

    private lateinit var manager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var receiver: WiFiDirectReceiver
    private lateinit var intentFilter: IntentFilter

    // 🔹 STATES (Moved to Activity level for stability and access)
    private var isProfileCreated by mutableStateOf(false)
    private var goToPermission by mutableStateOf(false)
    private var goToConnectionSetup by mutableStateOf(false)
    private var goToMessaging by mutableStateOf(false)

    private var hostAddress by mutableStateOf("")
    private var isGroupOwner by mutableStateOf(false)

    private var userName by mutableStateOf("")
    private var userAvatar by mutableStateOf<Uri?>(null)

    private val devices = mutableStateListOf<WifiP2pDevice>()

    private var isReceiverRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔥 INIT WIFI DIRECT
        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        // 🔥 INTENT FILTER
        intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }

        // 🔥 RECEIVER INIT
        receiver = WiFiDirectReceiver(
            manager,
            channel,
            onDeviceFound = { device ->
                Log.d("WiFiDirect", "Found: ${device.deviceName}")

                if (!devices.contains(device)) {
                    devices.add(device)
                }

                // 🔥 AUTO CONNECT FIRST DEVICE
                val config = WifiP2pConfig().apply {
                    deviceAddress = device.deviceAddress
                }

                if (hasPermissions()) {
                    manager.connect(
                        channel,
                        config,
                        object : WifiP2pManager.ActionListener {
                            override fun onSuccess() {
                                Log.d("WiFiDirect", "Connecting...")
                            }

                            override fun onFailure(reason: Int) {
                                Log.e("WiFiDirect", "Connect failed: $reason")
                            }
                        })
                }
            },

            onConnected = { ip, owner ->
                Log.d("WiFiDirect", "Connected: $ip Owner: $owner")

                hostAddress = ip
                isGroupOwner = owner

                goToConnectionSetup = false
                goToMessaging = true
            }
        )

        setContent {
            SilentShareTheme {

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

                    // 2️⃣ PERMISSION
                    goToPermission -> {
                        PermissionVerifiedScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onBack = { goToPermission = false },
                            onPermissionGranted = {
                                goToPermission = false
                                goToConnectionSetup = true
                            }
                        )
                    }

                    // 3️⃣ CONNECTION SETUP
                    goToConnectionSetup -> {
                        ConnectionSetupScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onBack = { goToConnectionSetup = false },
                            onConnected = {

                                // 🔥 START DISCOVERY HERE
                                if (hasPermissions()) {
                                    manager.discoverPeers(
                                        channel,
                                        object : WifiP2pManager.ActionListener {
                                            override fun onSuccess() {
                                                Log.d("WiFiDirect", "Discovery Started")
                                            }

                                            override fun onFailure(reason: Int) {
                                                Log.e("WiFiDirect", "Discovery Failed: $reason")
                                            }
                                        })
                                }
                            }
                        )
                    }

                    // 4️⃣ MESSAGING
                    goToMessaging -> {
                        MessagingScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            hostAddress = hostAddress,
                            isGroupOwner = isGroupOwner,
                            onBack = { goToMessaging = false }
                        )
                    }

                    // 5️⃣ HOME
                    else -> {
                        HomeScreen(
                            userName = userName,
                            avatarUri = userAvatar,
                            onCreateSession = {},
                            onJoinSession = { goToPermission = true },
                            onBack = {}
                        )
                    }
                }
            }
        }
    }

    private fun hasPermissions(): Boolean {
        val location = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val wifi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.NEARBY_WIFI_DEVICES
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        return location && wifi
    }

    override fun onResume() {
        super.onResume()
        if (!isReceiverRegistered) {
            // For Android 14+ we should specify export flag, though WifiP2P is a system broadcast
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED)
            } else {
                registerReceiver(receiver, intentFilter)
            }
            isReceiverRegistered = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (isReceiverRegistered) {
            unregisterReceiver(receiver)
            isReceiverRegistered = false
        }
    }
}
