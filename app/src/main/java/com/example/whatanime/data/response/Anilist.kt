package com.example.whatanime.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Anilist(
    @SerialName("id")
    val id: Int?,
    @SerialName("idMal")
    val idMal: Int?,
    @SerialName("isAdult")
    val isAdult: Boolean?,
    @SerialName("synonyms")
    val synonyms: List<String>?,
    @SerialName("title")
    val title: Title?
): Parcelable