package com.example.whatanime.ui.activities.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.whatanime.R
import com.example.whatanime.data.response.Result
import com.example.whatanime.ui.activities.detail.DetailActivity
import com.example.whatanime.ui.theme.WhatanimeTheme
import com.example.whatanime.ui.theme.secondaryColor
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
                NavHost(
                    navController = rememberNavController(),
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(viewModel)
                    }
//                    composable(Screen.AnimeDetail.route) {
//                        val anime = requireNotNull(navController.previousBackStackEntry?.arguments?.getParcelable<Result>("anime"))
//                        AnimeDetailScreen(anime)
//                    }
                }
            }
        }
    }

    @Composable
    fun HomeScreen(viewModel: MainViewModel) {
        Surface(
            color = Color.Black,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(275.dp)
                        .background(Color.Black),
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
                    ) {
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimeListScreen(viewModel.mainUiState)
                }
            }
        }
    }

    @Composable
    private fun AnimeListScreen(mainUiState: MainUiState) {
        when (mainUiState) {
            is MainUiState.Success -> AnimeList(mainUiState.anime)
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
            text = "Cari Anime Impian Anda!",
            color = secondaryColor,

            )
    }

    @Composable
    fun LoadingBar() {
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(text = "Loading")
        }
    }

    @Composable
    private fun SearchBar(
        modifier: Modifier = Modifier,
        hint: String = "",
        onSearch: (String) -> Unit = {},

        ) {
        var text by remember {
            mutableStateOf("")
        }
        var isHintDisplayed by remember {
            mutableStateOf(hint != "")
        }
        var warningVisible by remember { mutableStateOf(false) }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current

        val chooseImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { result ->
                if (result != null) {
                    val inputStream = contentResolver.openInputStream(result)
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
            BasicTextField(
                value = text,
                onValueChange = {
                    warningVisible = it.isNotEmpty()
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .size(width = 300.dp, height = 40.dp)
                    .background(Color.White, CircleShape)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
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
                        onSearch(text)
                        viewModel.getAnimesByUrl("https://images.plurk.com/32B15UXxymfSMwKGTObY5e.jpg")
                    }
                ),
            )
            if (isHintDisplayed) {
                Text(
                    text = hint,
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
            if (warningVisible) {
                Text(
                    text = "Paste URL or Image Here",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 12.dp, top = 40.dp)
                )
            }
            Icon(
                painterResource(
                    id = R.drawable.folder
                ),
                contentDescription = "search file",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(48.dp)
                    .align(CenterEnd)
                    .fillMaxWidth()
                    .padding(end = 20.dp, bottom = 10.dp)
                    .clickable { chooseImageLauncher.launch("image/*") }
            )
        }
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
        Card(modifier = modifier
            .padding(18.dp)
            .background(color = secondaryColor)
            .clickable {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("ANIME", anime)
                startActivity(intent)
            }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(anime.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "anime image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = modifier
                        .align(CenterHorizontally),
                    text = anime.anilist?.title?.english ?: "anime title"
                )
            }
        }
    }
}