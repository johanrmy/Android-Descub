package com.proyecto.integrador.descub.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.proyecto.integrador.descub.R

class ScannerActivity : AppCompatActivity() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        barcodeView = findViewById(R.id.barcode_scanner_view)
        resultTextView = findViewById(R.id.result_text_view)

        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                val qrCodeText = result.text
                Log.d(TAG, "QR Code: $qrCodeText")

                val intent = Intent(this@ScannerActivity, MuralActivity::class.java)
                intent.putExtra("qrCodeText", qrCodeText)
                startActivity(intent)
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
                // No hacer nada
            }
        })
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            barcodeView.resume()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeView.resume()
            } else {
                Toast.makeText(
                    this,
                    "Se requiere permiso de la cámara para escanear códigos QR",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        private const val TAG = "ScannerActivity"
    }
}
