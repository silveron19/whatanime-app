package com.example.whatanime.ui.activities.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.whatanime.data.response.Result
import com.example.whatanime.ui.theme.WhatanimeTheme

class DetailActivity : ComponentActivity() {
    private var selectedAnime: Result? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedAnime = intent.getParcelableExtra("anime")
        setContent {
            WhatanimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailScreen()
                }
            }
        }
    }
    @Composable
    fun DetailScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(selectedAnime?.image)
                    .crossfade(true)
                    .build(), contentDescription = selectedAnime?.anilist?.title?.english,
                modifier = Modifier
                    .width(400.dp)
                    .height(600.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(text = selectedAnime?.title.toString(), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = selectedAnime?.release_date.toString(), style = MaterialTheme.typography.bodyMedium)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = "Rating: ${selectedAnime?.vote_average.toString()}", style = MaterialTheme.typography.bodySmall)
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(text = selectedAnime?.overview.toString(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}