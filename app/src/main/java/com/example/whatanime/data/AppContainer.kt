package com.example.whatanime.data

import com.example.whatanime.data.remote.AnimeAPI
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.example.whatanime.data.repository.AnimeRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val animeRepository: AnimeRepository
}

class DefaultAppContainer: AppContainer {

    private val BASE_URL = "https://api.trace.moe"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService : AnimeAPI by lazy {
        retrofit.create(AnimeAPI::class.java)
    }

    override val animeRepository: AnimeRepository
        get() = AnimeRepository(retrofitService)

}