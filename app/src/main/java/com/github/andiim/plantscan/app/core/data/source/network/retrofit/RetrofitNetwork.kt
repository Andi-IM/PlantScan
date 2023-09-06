package com.github.andiim.plantscan.app.core.data.source.network.retrofit

import com.github.andiim.plantscan.app.BuildConfig
import com.github.andiim.plantscan.app.core.data.source.network.NetworkDataSource
import com.github.andiim.plantscan.app.core.data.source.network.model.DetectionResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private interface RetrofitNetworkApi {
    @Multipart
    @POST("orchid-flower-detection/1")
    fun uploadImage(
        @Part("api_key") apiKey: String,
        @Part image: MultipartBody.Part
    ): NetworkResponse<DetectionResponse>
}

private const val BASE_URL = BuildConfig.BACKEND_URL
private const val API_KEY = BuildConfig.ROBOFLOW_API

@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

@Singleton
class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okHttpCallFactory: Call.Factory,
) : NetworkDataSource {
    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okHttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitNetworkApi::class.java)

    override suspend fun detect(image: File): DetectionResponse {
        val imageRequestBody = image.asRequestBody("image/*".toMediaType())
        val imagePart = MultipartBody.Part.createFormData("image", image.name, imageRequestBody)
        val result = networkApi.uploadImage(apiKey = API_KEY, image = imagePart)
        return result.data
    }
}