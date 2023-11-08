package com.github.andiim.plantscan.core.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.util.Base64
import java.io.ByteArrayOutputStream

fun Context.getBitmap(uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                contentResolver,
                uri,
            ),
        ) { decoder, _, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true
        }
    } else {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }
}

fun Bitmap.asBase64(compressionLevel: Int = 20): String {
    val outputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, compressionLevel, outputStream)
    return Base64.getEncoder().encodeToString(outputStream.toByteArray())
}

fun String.asImageFromBase64(): Bitmap {
    val byteArray = Base64.getDecoder().decode(this)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}
