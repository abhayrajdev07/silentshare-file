package com.example.silentshare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ChatForegroundService : Service() {

    // 🔥 Jab bhi service start hogi, ye function call hoga
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // 1. Notification Channel banao (Android 8.0+ ke liye zaroori hai)
        createNotificationChannel()

        // 2. Notification design karo jo upar status bar me dikhega
        val notification = NotificationCompat.Builder(this, "ChatServiceChannel")
            .setContentTitle("SilentShare is Active")
            .setContentText("Your offline chat is running in the background.")
            .setSmallIcon(android.R.drawable.stat_notify_chat) // Default icon, tum ise badal sakte ho
            .setPriority(NotificationCompat.PRIORITY_LOW) // Low priority = Baar-baar sound nahi karega
            .setOngoing(true) // User isko swipe karke hata nahi payega (jab tak app background me hai)
            .build()

        // 3. Service ko Foreground mode me daal do (ID hamesha > 0 honi chahiye)
        startForeground(1, notification)

        // 4. START_STICKY ka matlab hai: Agar OS memory ke chakkar me ise kill kare,
        // to automatically wapas start kar de! 💀🛡️
        return START_STICKY
    }

    // Hum bound service use nahi kar rahe, isliye return null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // 🔥 Notification Channel Setup
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "ChatServiceChannel",
                "SilentShare Background Service",
                NotificationManager.IMPORTANCE_LOW
            )

            // System se Notification Manager maango aur channel create karo
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }
    }
}