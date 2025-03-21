package com.bm.arcgis_sample.data.remote


import com.arcgismaps.geometry.Point
import com.bm.arcgis_sample.domain.utill.Constants
import com.bm.arcgis_sample.data.repository.model.DtoReverseGeocodeResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET
    suspend fun getAddressFromReverseGeoCode(@Url url: String): Response<DtoReverseGeocodeResponse>
}

object Webservice {
    private const val WS_GET_REVERSE_GEOCODE_API = "reverseGeocode?"

    fun getReverseCodeApiUrl(location: Point): String {
        return buildString {
            append(WS_GET_REVERSE_GEOCODE_API)
            append("f=json")
            append("&token=${Constants.API_KEY}")
            append("&location=${location.x},${location.y}")
        }
    }
}

object NetworkClient {
    private const val BASE_URL_GEOCODE = "https://geocode-api.arcgis.com/arcgis/rest/services/World/GeocodeServer/"

    private val logger by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_GEOCODE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .readTimeout(5L, TimeUnit.SECONDS)
                    .connectTimeout(5L, TimeUnit.SECONDS)
                    .writeTimeout(5L, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(ApiService::class.java)
    }
}