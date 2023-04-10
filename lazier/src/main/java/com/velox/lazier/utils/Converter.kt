package com.velox.lazier.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream


/**
 * [toByteArray] Converting Uri to ByteArray
 * */
@SuppressLint("Recycle")
fun Uri.toByteArray(context: Context): ByteArray? {
    val iStream = context.contentResolver.openInputStream(this)
    val byteBuffer = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)
    var len: Int
    return if (iStream != null) {
        while (iStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        byteBuffer.toByteArray()
    } else {
        null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun ByteArray.toBase64(): String = String(java.util.Base64.getEncoder().encode(this))






fun String?.toEncodedBase64(): String?{
    val bytes = this?.toByteArray(Charsets.UTF_8)
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun String?.toDecodedBase64(): String {
    val bytes = this.let { Base64.decode(this, Base64.DEFAULT) }
    return String(bytes, Charsets.UTF_8)
}

fun String?.toBitMapFromBase64(): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}



fun Bitmap?.cropCenter(): Bitmap? {
    return try {
        val centerX = (this?.width ?: 0) / 2
        val centerY = (this?.height ?: 0) / 2
        val width = 200
        val height = 200
        val left = centerX - (width / 2)
        val top = centerY - (height / 2)
        val right = centerX + (width / 2)
        val bottom = centerY + (height / 2)
        val croppedBitmap = Bitmap.createBitmap(this!!, left, top, width, height)
        croppedBitmap
    } catch (e: Exception) {
        null
    }

}

fun Bitmap?.toByteArray(): ByteArray? {
    try {
        ByteArrayOutputStream().use { stream ->
            this?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            return stream.toByteArray()
        }
    } catch (e: Exception) {
        return null
    }
}

/*
fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}*/
