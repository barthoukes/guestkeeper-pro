package com.guestkeeper.pro.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.guestkeeper.pro.databinding.ActivityCameraBinding
import com.guestkeeper.pro.utils.CameraUtils
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var outputFile: File

    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filePath = intent.getStringExtra("photo_file_path")
        outputFile = File(filePath ?: getOutputFilePath())

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CAMERA_PERMISSION
            )
        }

        setupUI()
    }

    private fun getOutputFilePath(): String {
        val timeStamp = System.currentTimeMillis()
        val storageDir = getExternalFilesDir(null)
        return File(storageDir, "JPEG_${timeStamp}.jpg").absolutePath
    }

    private fun setupUI() {
        binding.btnCapture.setOnClickListener {
            takePhoto()
        }

        binding.btnRetake.setOnClickListener {
            binding.previewView.visibility = android.view.View.VISIBLE
            binding.ivCapturedImage.visibility = android.view.View.GONE
            binding.btnRetake.visibility = android.view.View.GONE
            binding.btnAccept.visibility = android.view.View.GONE
            binding.btnCapture.visibility = android.view.View.VISIBLE
        }

        binding.btnAccept.setOnClickListener {
            acceptPhoto()
        }

        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.btnSwitchCamera.setOnClickListener {
            // TODO: Implement camera switch
        }

        binding.btnFlash.setOnClickListener {
            // TODO: Implement flash toggle
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
                Toast.makeText(this, "Camera initialization failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: android.net.Uri.fromFile(outputFile)
                    
                    // Rotate image if needed
                    val rotatedBitmap = CameraUtils.rotateImageIfRequired(outputFile)
                    
                    runOnUiThread {
                        binding.previewView.visibility = android.view.View.GONE
                        binding.ivCapturedImage.visibility = android.view.View.VISIBLE
                        binding.btnCapture.visibility = android.view.View.GONE
                        binding.btnRetake.visibility = android.view.View.VISIBLE
                        binding.btnAccept.visibility = android.view.View.VISIBLE
                        
                        if (rotatedBitmap != null) {
                            binding.ivCapturedImage.setImageBitmap(rotatedBitmap)
                            // Save rotated bitmap
                            CameraUtils.saveBitmapToFile(rotatedBitmap, outputFile)
                        } else {
                            binding.ivCapturedImage.setImageURI(savedUri)
                        }
                        
                        Toast.makeText(
                            this@CameraActivity,
                            "Photo captured successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                    runOnUiThread {
                        Toast.makeText(
                            this@CameraActivity,
                            "Photo capture failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }

    private fun acceptPhoto() {
        val resultIntent = Intent()
        resultIntent.putExtra("photo_path", outputFile.absolutePath)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission is required to take photos",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}

