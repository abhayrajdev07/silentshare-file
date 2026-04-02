package com.example.silentshare

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentshare.ui.theme.SilentShareTheme
import com.example.silentshare.ui.theme.UniverseGradient
import com.example.silentshare.ui.theme.White
import com.google.rpc.Help
import androidx.compose.material.icons.filled.Help
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip

class AfterSplash : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SilentShareTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AfterSplashScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun AfterSplashScreen(innerPadding: PaddingValues = PaddingValues()) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UniverseGradient)
            .padding(innerPadding)
    ) {

        // 🔹 Help Icon


                Image(
                    painter = painterResource(id = R.drawable.bookhelp),
                    contentDescription = "Help",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 5.dp, end = 10.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable {
                            context.startActivity(Intent(context, GuideActivity::class.java))
                        }
                )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logoss1),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("India's First", color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            Text("Privacy Focused", color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            Text("Offline Messaging App", color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(200.dp))


            Button(
                onClick = {
                    context.startActivity(
                        Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    )
                    (context as? ComponentActivity)?.finish()
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = White),
                modifier = Modifier
                    .height(55.dp)
                    .width(330.dp)
            ) {
                Text("Get Started", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}