package com.example.silentshare

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.silentshare.ui.theme.BlueGradient
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun ConnectionSetupKtorScreen(
    userName: String,
    avatarUri: Uri?,
    onCreateServer: () -> Unit,
    onJoinServer: (String) -> Unit,
    onBack: () -> Unit
) {
    // Intercepts the system back gesture and calls your navigation logic
    BackHandler {
        onBack()
    }
    
    // 🔥 Auto-fetch Host IP
    val myIpAddress = remember { NetworkUtils.getLocalIpAddress() }
    val qrBitmap = rememberQrBitmap(text = myIpAddress)
    val context = LocalContext.current

    // Auto-fetch Host's Gateway IP for Client
    val gatewayIp = remember { NetworkUtils.getGatewayIp(context) }

    // IP input state (Isme default value gatewayIp set kar do)
    var ipInput by remember { mutableStateOf(gatewayIp) }

    // 🔥 QR Scanner Launcher
    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            // Scanner se jo IP mila, use direct join karwa do
            onJoinServer(result.contents)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(userName, avatarUri, onBack)

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // ----------------------------------------
                // 🔹 SECTION 1: HOST (SHOW QR CODE)
                // ----------------------------------------
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

                        // Show QR Image
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
                        Button(
                            onClick = onCreateServer,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Start Chat as Host")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                Text("OR", color = Color.White)
                Spacer(modifier = Modifier.height(40.dp))

                // ----------------------------------------
                // 🔹 SECTION 2: CLIENT (SCAN QR OR ENTER IP)
                // ----------------------------------------
                TextField(
                    value = ipInput,
                    onValueChange = { ipInput = it },
                    placeholder = { Text("Enter Server IP") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = { onJoinServer(ipInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Join with IP")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 🔥 SCAN QR BUTTON
                    Button(
                        onClick = {
                            val options = ScanOptions()
                            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            options.setPrompt("Scan the Host's QR Code")
                            options.setCameraId(0) // Use specific camera of the device
                            options.setBeepEnabled(true)
                            options.setBarcodeImageEnabled(true)
                            barcodeLauncher.launch(options)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A884)) // WhatsApp Green
                    ) {
                        Text("Scan QR", color = Color.White)
                    }
                }
            }
        }
    }
}