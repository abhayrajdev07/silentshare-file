package com.example.silentshare

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.util.Log

class WiFiDirectReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val onDeviceFound: (WifiP2pDevice) -> Unit,
    private val onConnected: (hostAddress: String, isGroupOwner: Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.action) {
            // 🔍 DEVICE DISCOVERY
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                manager.requestPeers(channel) { peers ->
                    if (peers != null && peers.deviceList.isNotEmpty()) {
                        Log.d("WiFiDirect", "Devices found: ${peers.deviceList.size}")
                        peers.deviceList.forEach { device ->
                            onDeviceFound(device)
                        }
                    } else {
                        Log.d("WiFiDirect", "No devices found")
                    }
                }
            }

            // 🔗 CONNECTION STATE CHANGE
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                val networkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        WifiP2pManager.EXTRA_NETWORK_INFO,
                        NetworkInfo::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO)
                }

                if (networkInfo?.isConnected == true) {
                    Log.d("WiFiDirect", "Connected to peer")
                    manager.requestConnectionInfo(channel) { info ->
                        if (info != null && info.groupFormed) {
                            val hostIP = info.groupOwnerAddress?.hostAddress ?: ""
                            val isOwner = info.isGroupOwner
                            Log.d(
                                "WiFiDirect",
                                "Group formed | Host IP: $hostIP | IsOwner: $isOwner"
                            )
                            onConnected(hostIP, isOwner)
                        }
                    }
                } else {
                    Log.d("WiFiDirect", "Disconnected or connection in progress...")
                }
            }

            // 📡 WIFI DIRECT STATE CHANGE
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(
                    WifiP2pManager.EXTRA_WIFI_STATE,
                    WifiP2pManager.WIFI_P2P_STATE_DISABLED
                )
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Log.d("WiFiDirect", "WiFi Direct is ENABLED ✅")
                } else {
                    Log.d("WiFiDirect", "WiFi Direct is DISABLED ❌")
                }
            }

            // 📱 DEVICE INFO CHANGE
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        WifiP2pManager.EXTRA_WIFI_P2P_DEVICE,
                        WifiP2pDevice::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)
                }
                device?.let {
                    Log.d("WiFiDirect", "This device: ${it.deviceName} Status: ${it.status}")
                }
            }
        }
    }
}