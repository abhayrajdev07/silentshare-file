package com.example.silentshare

import android.content.Context
import android.net.wifi.WifiManager

object NetworkUtils {

    // 🔥 1. FOR CLIENT: Router/Hotspot (Host) ka Gateway IP nikalne ke liye
    fun getGatewayIp(context: Context): String {
        try {
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val dhcp = wifiManager.dhcpInfo

            if (dhcp != null && dhcp.gateway != 0) {
                val ipInt = dhcp.gateway
                // Integer ko wapas IP String (192.168.x.x) mein convert karna
                return String.format(
                    "%d.%d.%d.%d",
                    ipInt and 0xff,
                    ipInt shr 8 and 0xff,
                    ipInt shr 16 and 0xff,
                    ipInt shr 24 and 0xff
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        // Agar kuch na mile toh default Hotspot IP return karo
        return "192.168.43.1"
    }

    // 🔥 2. FOR HOST: Khud ka IP nikalne ke liye (QR Code generate karne ke liye)
    fun getLocalIpAddress(): String {
        try {
            val interfaces =
                java.util.Collections.list(java.net.NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = java.util.Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress && addr is java.net.Inet4Address) {
                        return addr.hostAddress ?: "192.168.43.1"
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "192.168.43.1"
    }
}