package com.github.andiim.plantscan.app.core.data.source.network.retrofit

import com.github.andiim.plantscan.app.BuildConfig
import com.github.andiim.plantscan.app.core.data.source.network.NetworkDataSource
import com.github.andiim.plantscan.app.core.data.source.network.model.DetectionResponse
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.trace
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Base64
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitNetworkApi {
    @POST("/orchid-flower-detection/1")
    suspend fun uploadImage(
        @Query("api_key") apiKey: String,
        @Body base64Image: String
    ): DetectionResponse
}

private const val BASE_URL = BuildConfig.BACKEND_URL
private const val API_KEY = BuildConfig.ROBOFLOW_API

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {
    @Provides
    @Singleton
    fun provideLogging(): OkHttpClient {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            // HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}

@Singleton
class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okHttpCallFactory: Call.Factory,
    client: OkHttpClient,
) : NetworkDataSource {
    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okHttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .client(client)
        .build()
        .create(RetrofitNetworkApi::class.java)

    override suspend fun detect(image: String): DetectionResponse = trace("Detecting") {
        if (image.isValidBase64()) {
            return networkApi.uploadImage(apiKey = API_KEY, base64Image = image)
        }
        throw Exception("Not a valid base64 image file!")
    }
}

fun String.isValidBase64(): Boolean {
    return try {
        val decodedBytes = Base64.getDecoder().decode(this)
        String(decodedBytes)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}
