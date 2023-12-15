package com.example.whatanime.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Title(
    @SerialName("english")
    val english: String?,
    @SerialName("native")
    val native: String?,
    @SerialName("romaji")
    val romaji: String?
): Parcelable