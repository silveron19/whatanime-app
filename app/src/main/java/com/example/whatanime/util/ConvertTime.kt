package com.example.whatanime.util

fun doubleToTime(doubleValue: Double?): String {
    doubleValue?.let {
        val hours = (doubleValue.toInt() / 3600)
        val minutes = ((doubleValue.toInt() % 3600) / 60)
        val seconds = (doubleValue.toInt() % 60)

        return "$hours:$minutes:$seconds"
    } ?: run {
        return "Admin tidak tahu"
    }
}