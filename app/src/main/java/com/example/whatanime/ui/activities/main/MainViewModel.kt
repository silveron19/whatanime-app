package com.example.whatanime.ui.activities.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.whatanime.WhatanimeApplication
import com.example.whatanime.data.repository.AnimeRepository
import com.example.whatanime.data.response.Result
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.IOException

sealed interface MainUiState {
    data class Success(val anime: List<Result>) : MainUiState
    object Error : MainUiState
    object Loading : MainUiState
    object Empty: MainUiState
}

class MainViewModel(private val animeRepository: AnimeRepository): ViewModel() {

    var mainUiState: MainUiState by mutableStateOf(MainUiState.Empty)
        private set

    fun getAnimesByUrl(url: String) = viewModelScope.launch {
        mainUiState = MainUiState.Loading
        try {
            Log.d("MainViewModel", "url: ${url}")
            val result = animeRepository.getAnimeByUrl(
                cutBorder = true,
                anilistInfo = true,
                url = url
            )
            if(result.result!!.isEmpty()) {
                mainUiState = MainUiState.Empty
            }else {
                Log.d("MainViewModel", "url: ${result}")
                Log.d("MainViewModel", "getAnime: ${result.result.size}")
                mainUiState = MainUiState.Success(result.result.orEmpty())
            }
        } catch (e: IOException) {
            Log.d("MainViewMode", "getAnime error: ${e.message}")
            mainUiState = MainUiState.Error
        }
    }
    fun postAnimesByImage(url: MultipartBody.Part) = viewModelScope.launch {
        mainUiState = MainUiState.Loading
        try {
            Log.d("MainViewModel", "url: ${url}")
            val result = animeRepository.postAnimeByImage(
                cutBorder = true,
                anilistInfo = true,
                image = url
            )
            Log.d("ResponseDebug", "JSON Response: $result")
            if(result.result?.isEmpty() == true) {
                mainUiState = MainUiState.Empty
            } else {
                mainUiState = MainUiState.Success(result.result.orEmpty())
            }
        } catch (e: IOException) {
            Log.d("MainViewMode", "getAnime error: ${e.message}")
            mainUiState = MainUiState.Error
        } catch (e: Exception) {
            Log.d("MainViewMode", "Unexpected error: ${e.message}")
            mainUiState = MainUiState.Error
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WhatanimeApplication)
                val animeRepository = application.container.animeRepository
                MainViewModel(animeRepository)
            }
        }
    }
}