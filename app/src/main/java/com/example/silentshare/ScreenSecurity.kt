package com.example.silentshare

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ScreenSecurity : Application() {

    override fun onCreate() {
        super.onCreate()

        // App ki saari activities ko intercept karne ke liye callback register karein
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // Screenshot aur screen recording block karne ke liye FLAG_SECURE apply karein
//                activity.window.setFlags(
//                    WindowManager.LayoutParams.FLAG_SECURE,
//                    WindowManager.LayoutParams.FLAG_SECURE
//                )
            }

            // Interface ke baaki methods implement karna zaroori hai, par hum inhe khali chhod sakte hain
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}