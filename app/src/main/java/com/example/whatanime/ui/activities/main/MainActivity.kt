package com.example.whatanime.ui.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.whatanime.R
import com.example.whatanime.data.response.Result
import com.example.whatanime.ui.activities.detail.DetailActivity
import com.example.whatanime.ui.theme.WhatanimeTheme
import com.example.whatanime.ui.theme.secondaryColor
import com.example.whatanime.util.checkData
import com.example.whatanime.util.doubleToTime
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, MainViewModel.Factory)[MainViewModel::class.java]
        setContent {
            WhatanimeTheme {
                Surface(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(275.dp),
                            horizontalAlignment = CenterHorizontally,
                        ) {
                            Image(
                                painterResource(id = R.drawable.logo_whatanime),
                                contentDescription = "Anime",
                                modifier = Modifier
                                    .size(200.dp)
                                    .align(CenterHorizontally)
                            )
                            SearchBar(
                                hint = "Search",
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.secondary),
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AnimeScreen(viewModel.mainUiState)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun AnimeScreen(mainUiState: MainUiState) {
        when (mainUiState) {
            is MainUiState.Success -> mainUiState.anime?.let { AnimeList(it) }
            is MainUiState.Error -> ErrorText()
            is MainUiState.Loading -> LoadingBar()
            is MainUiState.Empty -> EmptyText()
            else -> {
                EmptyText()
            }
        }
    }
    @Composable
    private fun ErrorText() {
        Text(
            text = "Ups! Something Went Wrong!",
            color = secondaryColor,
        )
    }
    @Composable
    private fun EmptyText() {
        Text(
            text = "Selamat Datang Sepuh!^^",
            color = secondaryColor,
            fontWeight = FontWeight.Bold
            )
    }
    @Composable
    fun LoadingBar() {
        Box(contentAlignment = Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun SearchBar(
        modifier: Modifier = Modifier,
        hint: String = "",
    ) {
        var text by remember { mutableStateOf("") }
        var isHintDisplayed by remember { mutableStateOf(hint != "") }
        var warningVisible by remember { mutableStateOf(false) }
        val context = LocalContext.current

        val chooseImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { result ->
                if (result != null) {
                    val inputStream = context.contentResolver.openInputStream(result)
                    val bytes = inputStream?.readBytes()
                    inputStream?.close()

                    if (bytes != null) {
                        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                        val part =
                            MultipartBody.Part.createFormData("image", "demo.jpg", requestBody)
                        Log.d("imageUpload", "image: $result")
                        showMessage(context, "Image selected")
                        viewModel.postAnimesByImage(part)
                    }
                }
            }
        )
        Box(modifier = modifier) {
            var keyboardController = LocalSoftwareKeyboardController.current
            Row {
                BasicTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier
                        .size(width = 300.dp, height = 40.dp)
                        .background(Color.White, CircleShape)
                        .padding(top = 10.dp, start = 15.dp)
                        .onFocusChanged {
                            isHintDisplayed = !it.isFocused
                        },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if(text.isNotEmpty()){
                                viewModel.getAnimesByUrl(text)
                                text = ""
                                keyboardController?.hide()
                            }else{
                                warningVisible = true
                            }
                        }
                    ),
                )
                Icon(
                    painterResource(id = R.drawable.folder),
                    contentDescription = "search file",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { chooseImageLauncher.launch("image/*") }
                        .padding(start = 10.dp)
                )
            }
            if (isHintDisplayed) {
                Text(
                    text = hint,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 8.dp, start = 15.dp)
                )
            }
            if (warningVisible) {
                Text(
                    text = "Invalid URL: https and image extension require",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 40.dp, start = 15.dp)
                )
            }
        }
    }

    private fun validateUrl(url: String): Boolean {
        val isValidUrl = Patterns.WEB_URL.matcher(url).matches() && url.startsWith("https://")
        return (isValidUrl)
    }
    private fun isImageExtension(url: String): Boolean {
        val imageExtensions = listOf(".jpg", ".jpeg", ".png", ".gif", ".bmp")
        return imageExtensions.any { url.endsWith(it, ignoreCase = true) }
    }
    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @Composable
    private fun AnimeList(animes: List<Result>, modifier: Modifier = Modifier) {
        LazyColumn(modifier = modifier) {
            items(animes) { anime ->
                AnimeCard(anime = anime)
            }
        }
    }

    @Composable
    private fun AnimeCard(anime: Result, modifier: Modifier = Modifier) {
        val endingTime = doubleToTime(anime.to)
        val startingTime = doubleToTime(anime.from)
        Card(modifier = modifier
            .padding(18.dp)
            .clickable {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("anime", anime)
                startActivity(intent)
            },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )

        ) {
            Column{
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(anime.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "anime image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Text(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = modifier
                        .align(CenterHorizontally)
                        .padding(horizontal = 10.dp),
                    text = "${checkData(anime.anilist?.title?.english)} (Episode ${anime.episode ?: "Admin lupa :v"})" ,
                    textAlign = TextAlign.Center
                )
                Text(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = modifier
                        .align(CenterHorizontally),
                    color = Color(0xFF086900),
                    text = "${checkData(startingTime)} - ${checkData(endingTime)}",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}