package com.guestkeeper.pro.utils

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

object CameraUtils {

    suspend fun capturePhoto(
        context: Context,
        fileName: String
    ): File? = withContext(Dispatchers.IO) {
        try {
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val photoFile = File.createTempFile(
                "JPEG_${fileName}_",
                ".jpg",
                storageDir
            )
            photoFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    suspend fun compressImage(
        context: Context,
        inputFile: File,
        maxSize: Long = 1024 * 1024 // 1MB
    ): File = withContext(Dispatchers.IO) {
        return@withContext Compressor.compress(context, inputFile) {
            quality(80)
            maxWidth(1024)
            maxHeight(1024)
        }
    }

    fun getImageUri(context: Context, file: File): Uri {
        return Uri.fromFile(file)
    }

    fun rotateImageIfRequired(file: File): Bitmap? {
        return try {
            val exif = ExifInterface(file.absolutePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val matrix = Matrix()

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }

            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createThumbnail(bitmap: Bitmap, size: Int = 100): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scale = size.toFloat() / minOf(width, height)

        val matrix = Matrix()
        matrix.postScale(scale, scale)

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            width,
            height,
            matrix,
            true
        )
    }

    fun saveBitmapToFile(bitmap: Bitmap, file: File): Boolean {
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun getFileSizeInMB(file: File): Double {
        return file.length().toDouble() / (1024 * 1024)
    }

    fun isStorageAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun getAvailableStorageSpace(): Long {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        return stat.availableBlocksLong * stat.blockSizeLong
    }
}

