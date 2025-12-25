package com.hfad.rk2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("gifs/trending")
    suspend fun getGifs(
        @Query("api_key") apiKey: String = "6jMa9v9V4TII6XzKEbgErZLyfJ7cg6MQ",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): GiphyResponse
}

object ApiClient {
    private const val BASE_URL = "https://api.giphy.com/v1/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}