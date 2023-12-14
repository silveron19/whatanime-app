package com.example.whatanime.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class getAnimeResponse(
    @SerialName("error")
    val error: String?,
    @SerialName("frameCount")
    val frameCount: Int?,
    @SerialName("result")
    val result: List<Result>?
)