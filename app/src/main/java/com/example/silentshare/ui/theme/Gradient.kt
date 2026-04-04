package com.example.silentshare.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val UniverseGradient = Brush.verticalGradient(
    colors = listOf(NeonMint, DeepElectricBlue)
)
//
//val BlueGradient = Brush.horizontalGradient(
//    colors = listOf(
//        Color(0xFF11538D), // Dark Blue
//        Color(0xFF52C1F9)  // Light Blue
//    )
//)

val BlueGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF11538D),
        Color(0xFF52C1F9)
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)