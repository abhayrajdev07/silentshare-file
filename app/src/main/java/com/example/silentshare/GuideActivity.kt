package com.example.silentshare

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.silentshare.ui.theme.SilentShareTheme

class GuideActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Modern Edge-to-Edge handling
        enableEdgeToEdge()

        setContent {
            SilentShareTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent
                ) { innerPadding ->

                    // 👇 HERE IS THE FIX: Passing innerPadding and onClose
                    GuideScreen(
                        innerPadding = innerPadding,
                        onClose = {
                            startActivity(Intent(this@GuideActivity, AfterSplash::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}
