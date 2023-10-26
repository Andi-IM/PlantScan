package com.github.andiim.plantscan.core.network.retrofit

import com.github.andiim.plantscan.core.network.AppNetworkDataSource
import com.github.andiim.plantscan.core.network.BuildConfig
import com.github.andiim.plantscan.core.network.model.DetectionResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitNetworkApi {
    @POST("/orchid-flower-detection/3")
    suspend fun uploadImage(
        @Query("api_key") apiKey: String,
        @Query("confidence") confidence: Int,
        @Body base64Image: String
    ): DetectionResponse
}

@Singleton
class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okHttpCallFactory: Call.Factory,
    client: OkHttpClient,
) : AppNetworkDataSource {

    companion object {
        private const val BASE_URL: String = BuildConfig.BACKEND_URL
        private const val API_KEY: String = BuildConfig.ROBOFLOW_API
        private const val DETECT_TRACE_TAG: String = "detecting"
    }

    private val networkApi =
        Retrofit.Builder().baseUrl(BASE_URL).callFactory(okHttpCallFactory).addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        ).client(client).build().create(RetrofitNetworkApi::class.java)

    override suspend fun detect(image: String, confidence: Int): DetectionResponse =
        networkApi.uploadImage(
            apiKey = API_KEY,
            confidence = confidence,
            base64Image = image,
        )

}

@Suppress("SwallowedException")
private fun String.isValidBase64(): Boolean {
    return try {
        val decodedBytes = Base64.getDecoder().decode(this)
        String(decodedBytes)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}
