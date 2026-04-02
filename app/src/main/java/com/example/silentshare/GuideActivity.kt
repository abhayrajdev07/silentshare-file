package com.example.silentshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class GuideActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GuideScreen() // this is your guide UI
        }
    }
}