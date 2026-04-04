package com.example.silentshare

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.silentshare.ui.theme.Blue
import com.example.silentshare.ui.theme.BlueGradient

@Composable
fun ProfileScreen(
    onSave: (String, Uri?) -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image Picker Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueGradient)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Create Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Avatar Picker
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable {
                    launcher.launch("image/*")
                }
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Profile", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Name Input

        OutlinedTextField(
            value = name,
            onValueChange = {
                if (it.length <= 15) {   // 🔥 limit to 15 chars
                    name = it
                }
            },
            label = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(

                // ✨ TEXT COLOR (when typing)
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,

                // ✨ CURSOR
                cursorColor = Color.White,

                // ✨ BORDER
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,

                // ✨ LABEL
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray,

                // ✨ PLACEHOLDER (optional)
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Save Button
        Button(
            onClick = {
                if (name.isBlank()) {
                    Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show()
                } else {
                    onSave(name, imageUri)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,      // 🔥 button background
            contentColor = Color.White         // 🔥 text color
        )
        ) {
            Text("Save Profile")
        }
    }
}