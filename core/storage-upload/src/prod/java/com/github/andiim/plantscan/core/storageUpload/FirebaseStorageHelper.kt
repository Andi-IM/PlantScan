package com.github.andiim.plantscan.core.storageUpload

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FirebaseStorageHelper @Inject constructor(
    private val storage: FirebaseStorage,
) : StorageHelper {

    override fun upload(
        image: Bitmap,
        baseLocation: String,
        onProgress: ((Double) -> Double)?,
    ): Flow<String> = flow {
        val ref = storage.reference.child(baseLocation) // refers to base
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val data = outputStream.toByteArray()
        val downloadUrl = ref.putBytes(data).await().storage.downloadUrl.await()
        emit(downloadUrl.toString())
    }
}
