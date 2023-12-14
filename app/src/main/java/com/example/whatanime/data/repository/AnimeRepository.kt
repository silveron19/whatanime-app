package com.example.whatanime.data.repository

import com.example.whatanime.data.remote.AnimeAPI
import com.example.whatanime.data.response.getAnimeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AnimeRepository(private val api: AnimeAPI) {

    suspend fun getAnimeByUrl(cutBorder: Boolean, anilistInfo: Boolean, url: String): getAnimeResponse {
        return api.searchByUrl(cutBorder, anilistInfo, url)
    }
    suspend fun postAnimeByImage(cutBorder: Boolean, anilistInfo: Boolean, image: MultipartBody.Part): getAnimeResponse {
        return api.searchByFormPost(cutBorder, anilistInfo, image)
    }
//    suspend fun postAnimeByImage(cutBorder: Boolean, anilistInfo: Boolean, image: ByteArray): getAnimeResponse {
//        return api.searchByImage(cutBorder, anilistInfo, image)
//    }
}