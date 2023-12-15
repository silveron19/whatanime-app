package com.example.whatanime.util

fun checkData(data: String?): String{
    if(data === null) {
        return("Admin malas cari tahu :v")
    }else {
        return(data)
    }
}