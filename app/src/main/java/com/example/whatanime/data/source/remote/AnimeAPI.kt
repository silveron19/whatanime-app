package com.example.whatanime.data.remote

import com.example.whatanime.data.response.getAnimeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AnimeAPI {
    @Multipart
    @POST("search")
    suspend fun searchByFormPost(
        @Query("cutBorders") cutBorders: Boolean = true,
        @Query("anilistInfo") anilistInfo: Boolean = true,
        @Part image: MultipartBody.Part
    ): getAnimeResponse


//    @POST("search")
//    @Headers("Content-type: image/jpeg")
//    suspend fun searchByImage(
//        @Query("cutBorders") cutBorders: Boolean = true,
//        @Query("anilistInfo") anilistInfo: Boolean = true,
//        @Body image: ByteArray,
//    ): getAnimeResponse

    @GET("search")
    suspend fun searchByUrl(
        @Query("cutBorders") cutBorders: Boolean = true,
        @Query("anilistInfo") anilistInfo: Boolean = true,
        @Query("url") url: String,
    ): getAnimeResponse
}