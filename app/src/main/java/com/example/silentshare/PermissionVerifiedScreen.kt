package com.example.silentshare

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.silentshare.ui.theme.BlueGradient

@Composable
fun PermissionVerifiedScreen(
    userName: String,
    avatarUri: Uri?,
    onBack: () -> Unit,
    onPermissionGranted: () -> Unit
) {

    val context = LocalContext.current
    var allGranted by remember { mutableStateOf(false) }

    // 🔥 CHECK PERMISSIONS SAFELY (ALL ANDROID VERSIONS)
    fun checkPermissions(): Boolean {

        val location = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val wifi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.NEARBY_WIFI_DEVICES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // older devices don't need this
        }

        return location && wifi
    }

    // 🔥 REQUEST PERMISSIONS SAFELY
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        allGranted = result.values.all { it }

        // 🔥 AUTO NAVIGATION AFTER GRANT
        if (allGranted) {
            onPermissionGranted()
        }
    }

    // 🔥 INITIAL CHECK
    LaunchedEffect(Unit) {
        allGranted = checkPermissions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .statusBarsPadding()
    ) {

        // 🔝 TOP BAR
        TopBar(
            userName = userName,
            avatarUri = avatarUri,
            onBack = onBack
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFF0D0D0D))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Permissions Required",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 🔥 DYNAMIC PERMISSION REQUEST
                Button(
                    onClick = {

                        val permissions =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.NEARBY_WIFI_DEVICES
                                )
                            } else {
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            }

                        launcher.launch(permissions)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Grant Permissions")
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 🔹 STATUS INDICATOR
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ready to Connect", color = Color.White)

                    Spacer(modifier = Modifier.width(10.dp))

                    Switch(
                        checked = allGranted,
                        onCheckedChange = {},
                        enabled = false
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 🔥 FALLBACK BUTTON (OPTIONAL)
                if (allGranted) {
                    Button(
                        onClick = onPermissionGranted,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}