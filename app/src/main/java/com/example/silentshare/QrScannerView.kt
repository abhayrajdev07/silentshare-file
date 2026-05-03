package com.example.silentshare

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

@Composable
fun QrScannerView(
    modifier: Modifier = Modifier,
    onResult: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    AndroidView(
        modifier = modifier,
        factory = {
//            val barcodeView = DecoratedBarcodeView(context)

            val barcodeView = DecoratedBarcodeView(context)

// REMOVE DEFAULT TEXT
            barcodeView.setStatusText("")

// REMOVE RED LASER
            barcodeView.viewFinder.setLaserVisibility(false)
            // 🔥 Force portrait (no landscape)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            // 🔥 Only QR codes
            barcodeView.barcodeView.decoderFactory =
                DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE))

            // 🔥 Remove red laser line
            barcodeView.viewFinder.setLaserVisibility(false)

            // 🔥 Start camera
            barcodeView.resume()

            // 🔥 Continuous scan
            barcodeView.decodeContinuous { result ->
                result.text?.let { onResult(it) }
            }

            // Ensure it fills parent
            barcodeView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            barcodeView
        },
        update = {
            it.resume()
        },
        onRelease = {
            it.pause()
        }
    )
}