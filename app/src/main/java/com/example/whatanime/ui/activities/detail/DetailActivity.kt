package com.example.whatanime.ui.activities.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.whatanime.data.response.Result
import com.example.whatanime.util.checkData
import com.example.whatanime.util.doubleToTime
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

class DetailActivity : ComponentActivity() {
    private var selectedAnime: Result? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedAnime = intent.getParcelableExtra("anime")
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                contentColor = Color.Black,
                color = MaterialTheme.colorScheme.surface

            ) {
                DetailScreen()
            }
        }
    }

    @Composable
    fun DetailScreen() {
        val endingTime = doubleToTime(selectedAnime?.to)
        val startingTime = doubleToTime(selectedAnime?.from)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Column {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                    ,
                    factory = { context ->
                        val exoPlayer = SimpleExoPlayer.Builder(context).build().apply {
                            val mediaItem = MediaItem.fromUri(selectedAnime?.video?.toUri() ?: return@apply)
                            setMediaItem(mediaItem)
                            prepare()
                            playWhenReady = false
                        }

                        StyledPlayerView(context).apply {
                            player = exoPlayer
                        }
                    }
                )
                Spacer(modifier = Modifier.size(35.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = CenterHorizontally
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
                        Spacer(modifier = Modifier.size(35.dp))
                        Text(
                            text = "${checkData(selectedAnime?.anilist?.title?.native)}\n" +
                                    checkData(selectedAnime?.anilist?.title?.english),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(
                            text = "Episode: ${selectedAnime?.episode ?: "Admin tidak tahu :v"}",
                            fontSize = 18.sp,

                        )
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(
                            text = "Timestamp: ${checkData(startingTime)} - ${checkData(endingTime)}",
                            fontSize = 18.sp,
                        )
                    }
            }
        }
    }
}