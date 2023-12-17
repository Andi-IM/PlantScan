package com.github.andiim.plantscan.core.network.retrofit

import com.github.andiim.plantscan.core.network.BuildConfig
import com.github.andiim.plantscan.core.network.PsNetworkDataSource
import com.github.andiim.plantscan.core.network.model.AlgoliaRequest
import com.github.andiim.plantscan.core.network.model.DetectionResponse
import com.github.andiim.plantscan.core.network.model.QueryResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private const val API_KEY: String = BuildConfig.ROBOFLOW_API
private const val ALGOLIA_APP_ID: String = BuildConfig.ALGOLIA_APP_ID
private const val ALGOLIA_API_KEY: String = BuildConfig.ALGOLIA_API_KEY

private interface AlgoliaNetworkApi {
    @Headers(
        "X-Algolia-Application-Id:$ALGOLIA_APP_ID",
        "X-Algolia-API-Key:$ALGOLIA_API_KEY",
    )
    @POST("/1/indexes/prod_plants/query")
    suspend fun search(@Body query: AlgoliaRequest): QueryResponse
}

private interface RoboflowNetworkApi {
    @POST("/orchid-flower-detection/3")
    suspend fun uploadImage(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("confidence") confidence: Int,
        @Query("overlap") overlap: Int,
        @Body base64Image: String,
    ): DetectionResponse
}

@Singleton
class RetrofitPsNetwork @Inject constructor(
    networkJson: Json,
    okHttpCallFactory: Call.Factory,
    client: OkHttpClient,
) : PsNetworkDataSource {

    companion object {
        private const val BASE_URL: String = BuildConfig.BACKEND_URL
        private const val ALGOLIA_URL: String = BuildConfig.ALGOLIA_URL
    }

    private val algoliaApi = networkApi(
        ALGOLIA_URL,
        okHttpCallFactory,
        networkJson,
        client,
    ).create(AlgoliaNetworkApi::class.java)

    private val roboflowApi = networkApi(
        BASE_URL,
        okHttpCallFactory,
        networkJson,
        client,
    ).create(RoboflowNetworkApi::class.java)

    private fun networkApi(
        baseUrl: String,
        okHttpCallFactory: Call.Factory,
        networkJson: Json,
        client: OkHttpClient,
    ): Retrofit = Retrofit.Builder().baseUrl(baseUrl).callFactory(okHttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        ).client(client).build()

    override suspend fun detect(image: String, confidence: Int, overlap: Int): DetectionResponse =
        roboflowApi.uploadImage(
            confidence = confidence,
            overlap = overlap,
            base64Image = image,
        )

    override suspend fun search(query: String): QueryResponse =
        algoliaApi.search(AlgoliaRequest("query=$query"))
}
