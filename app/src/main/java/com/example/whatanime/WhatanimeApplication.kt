package com.example.whatanime

import android.app.Application
import com.example.whatanime.data.AppContainer
import com.example.whatanime.data.DefaultAppContainer

class WhatanimeApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

}