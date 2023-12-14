package com.example.whatanime.ui.activities.main

import androidx.compose.runtime.Composable
sealed class Screen (val route: String) {
    object Home : Screen("home")
    object AnimeDetail : Screen("anime_detail")
}

@Composable
fun AnimeApp(){
}
