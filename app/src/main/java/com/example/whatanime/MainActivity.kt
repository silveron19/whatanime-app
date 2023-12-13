package com.example.whatanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whatanime.ui.theme.WhatanimeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatanimeTheme {
                val navController = rememberNavController();
                NavHost(
                    navController = navController,
                    startDestination = "anime_list_screen")
                {
                    composable("anime_list_screen") {

                    }
                    composable("anime_detail_screen/{animeName}/{animeEpisode}",
                        arguments = listOf(
                            navArgument("animeName") {
                                type = NavType.StringType
                            },
                            navArgument("animeEpisode") {
                                type = NavType.StringType
                            },
                        )
                    ) {
                        val animeName = remember {
                            it.arguments?.getString("animeName")
                        }
                        val animeEpisode = remember {
                            it.arguments?.getString("animeEpisode", "unknown")
                        }
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column {
        Row (modifier = modifier
            .background(color = Color.Black)
            .fillMaxWidth()
            .height(250.dp)) {
            Image(painter = painterResource(id = R.drawable.logo_whatanime), contentDescription = "whatanime", modifier= modifier
                .fillMaxWidth()
                .padding(40.dp))
        }
        Row (modifier = modifier
            .background(color = colorResource(id = R.color.primaryColor))
            .size(width = 10.dp, height = 5.dp)
        ){

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhatanimeTheme {
        Greeting("Android")
    }
}