package com.example.ltp.weather2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.ltp.weather2.model.Weather
import com.example.ltp.weather2.service.WeatherService
import com.example.ltp.weather2.ui.theme.AppTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val weatherService by lazy { WeatherService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen(weatherService)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(weatherService: WeatherService) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding)
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            var weather by rememberSaveable { mutableStateOf<Weather?>(null) }
            val keyboardController = LocalSoftwareKeyboardController.current
            val scope = rememberCoroutineScope()

            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search a city") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            scope.launch {
                                weather = weatherService.getWeather(text)
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
            WeatherBoard(Modifier.fillMaxSize(), weather)
        }
    }
}

@Composable
fun WeatherBoard(modifier: Modifier = Modifier, weather: Weather? = null) {
    if (weather == null) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Text("No weather data", color = Color.Gray, fontSize = 30.sp)
        }
    } else {
        Column(
            modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${weather.city}, ${weather.countryCode}", fontSize = 30.sp)
            Text("${weather.temperature.roundToInt()}°C", fontSize = 80.sp)
            Text(weather.condition)
            Text("Humidity: ${weather.humidity}%", fontSize = 20.sp)
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen(WeatherService())
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyWeatherBoardPreview() {
    AppTheme {
        WeatherBoard()
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherBoardPreview() {
    AppTheme {
        val weather = Weather(
            "Singapore",
            "SG",
            26.0,
            80,
            "Clear"
        )
        WeatherBoard(weather = weather)
    }
}
