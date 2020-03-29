package com.sanfulou.qrcode2020.utils

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata


class QrCodeAnalyzer(send: Send) : ImageAnalysis.Analyzer {

    val send = send;

    var isQrcode = true

    override fun analyze(image: ImageProxy, rotationDegrees: Int) {

        if (!isQrcode) {
            return
        }

        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build()

        val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)

        val rotation = rotationDegreesToFirebaseRotation(rotationDegrees)
        val visionImage = FirebaseVisionImage.fromMediaImage(image.image!!, rotation)

        detector.detectInImage(visionImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes != null) {
                        if (barcodes.size > 0) {
                            send.sendBarcode(barcodes)
                            isQrcode = false
                        }

                    }

                }
                .addOnFailureListener {
                    Log.e("QrCodeAnalyzer", "something went wrong", it)
                }

    }

    private fun rotationDegreesToFirebaseRotation(rotationDegrees: Int): Int {
        return when (rotationDegrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw IllegalArgumentException("Not supported")
        }
    }

    interface Send {
        fun sendBarcode(i: List<FirebaseVisionBarcode>?)

    }
}