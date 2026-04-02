package com.example.silentshare

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pDevice

class WiFiDirectReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val onDeviceFound: (String, WifiP2pDevice) -> Unit,
    private val onConnected: (String, Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {

                manager.requestPeers(channel) { peers ->

                    if (peers.deviceList.isNotEmpty()) {

                        val device = peers.deviceList.first()
                        onDeviceFound(device.deviceName, device)

                    }
                }
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

                val networkInfo =
                    intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)

                if (networkInfo?.isConnected == true) {

                    manager.requestConnectionInfo(channel) { info ->

                        val hostIP = info.groupOwnerAddress.hostAddress
                        val isOwner = info.isGroupOwner

                        onConnected(hostIP, isOwner)
                    }
                }
            }
        }
    }
}
