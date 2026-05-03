package com.example.silentshare

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.silentshare.ui.theme.BlueGradient

@Composable
fun CreateSessionScreen(
    userName: String,
    avatarUri: Uri?,
    onCreateServer: () -> Unit,
    onBack: () -> Unit
) {
    // Intercepts the system back gesture
    BackHandler { onBack() }

    val myIpAddress = remember { NetworkUtils.getLocalIpAddress() }
    val qrBitmap = rememberQrBitmap(text = myIpAddress)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(userName, avatarUri, onBack)

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Ask your friend to scan this QR", color = Color.Black)
                    Spacer(modifier = Modifier.height(10.dp))

                    qrBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(200.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Or enter IP manually: $myIpAddress", color = Color.Gray)

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔥 This correctly maps to the onCreateServer parameter in MainActivity!
                    Button(
                        onClick = onCreateServer,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(
                            text = "Start Chat as Host",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}