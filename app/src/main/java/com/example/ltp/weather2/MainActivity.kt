package com.example.ltp.weather2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.ltp.weather2.model.Weather
import com.example.ltp.weather2.ui.theme.AppTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
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
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search a city") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
            WeatherBoard(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun WeatherBoard(modifier: Modifier = Modifier, weather: Weather? = null) {
    if (weather == null) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Text("No weather data")
        }
    } else {
        Column(
            modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${weather.city}, ${weather.countryCode}")
            Text("${weather.temperature.roundToInt()}°C")
            Text(weather.condition)
            Text("Humidity: ${weather.humidity}%")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen()
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
