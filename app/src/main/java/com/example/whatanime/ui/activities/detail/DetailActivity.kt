package com.example.whatanime.ui.activities.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.StackView
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.whatanime.R
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
        var playCount by remember { mutableStateOf(0) }
        val handler = remember { Handler(Looper.getMainLooper()) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White),
        ) {
            Box {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(250.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        factory = { context ->
                            VideoView(context).apply {
                                setVideoURI(selectedAnime?.video?.toUri())
                                setOnCompletionListener {
                                    if (playCount < 3) {
                                        handler.postDelayed({
                                            start()
                                        }, 3000)
                                        playCount++
                                    }
                                }
                                start()
                            }
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 160.dp, start = 20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(selectedAnime?.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = selectedAnime?.anilist?.title?.english,
                        modifier = Modifier
                            .size(200.dp)
                            .shadow(10.dp, shape = RoundedCornerShape(15.dp), clip = true),
                        contentScale = ContentScale.Crop
                    )
                }
            }
             Spacer(modifier = Modifier.height(16.dp))

            // Text(text = selectedAnime?.title.toString(), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
            // Spacer(modifier = Modifier.height(8.dp))
            // Text(text = selectedAnime?.release_date.toString(), style = MaterialTheme.typography.bodyMedium)
            // Spacer(modifier = Modifier.height(8.dp))
            // Text(text = "Rating: ${selectedAnime?.vote_average.toString()}", style = MaterialTheme.typography.bodySmall)
            // Spacer(modifier = Modifier.height(16.dp))
            // Text(text = selectedAnime?.overview.toString(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}