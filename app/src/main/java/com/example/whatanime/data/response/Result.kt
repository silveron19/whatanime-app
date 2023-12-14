package com.example.whatanime.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Result(
    @SerialName("anilist")
    val anilist: Anilist?,
    @SerialName("episode")
    val episode: Int?,
    @SerialName("filename")
    val filename: String?,
    @SerialName("from")
    val from: Double?,
    @SerialName("image")
    val image: String?,
    @SerialName("similarity")
    val similarity: Double?,
    @SerialName("to")
    val to: Double?,
    @SerialName("video")
    val video: String?
): Parcelable