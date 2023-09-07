package com.github.andiim.plantscan.app.core.data.source.network.retrofit

import android.graphics.Bitmap
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
import java.io.ByteArrayOutputStream
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

    override suspend fun detect(image: Bitmap): DetectionResponse = trace("Detecting") {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)

        val byteArray = outputStream.toByteArray()
        val base = Base64.getEncoder().encodeToString(byteArray)

        return networkApi.uploadImage(apiKey = API_KEY, base64Image = base)
    }
}