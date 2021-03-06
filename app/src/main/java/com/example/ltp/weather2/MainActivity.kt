package com.example.ltp.weather2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
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
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Weather") }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding)
        ) {
            var weather by rememberSaveable { mutableStateOf<Weather?>(null) }
            val keyboardController = LocalSoftwareKeyboardController.current
            val scope = rememberCoroutineScope()
            SearchBar(
                onSearch = {
                    keyboardController?.hide()
                    scope.launch {
                        if (it.isBlank()) {
                            scaffoldState.snackbarHostState
                                .showSnackbar("Please enter a city name.")
                        } else {
                            weather = weatherService.getWeather(it)
                            if (weather == null) {
                                scaffoldState.snackbarHostState
                                    .showSnackbar("Unable to load weather data.")
                            }
                        }
                    }
                }
            )
            WeatherBoard(Modifier.fillMaxSize(), weather)
        }
    }
}

@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by rememberSaveable { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search a city") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(text) }
        ),
        trailingIcon = {
            IconButton(
                onClick = { onSearch(text) }
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        }
    )
}

@Composable
fun WeatherBoard(modifier: Modifier = Modifier, weather: Weather? = null) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (weather == null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_cloud_off_24),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color.Gray
            )
            Text("No weather data", color = Color.Gray, fontSize = 30.sp)
        } else {
            Text("${weather.city}, ${weather.countryCode}", fontSize = 30.sp)
            Text("${weather.temperature.roundToInt()}??C", fontSize = 80.sp)
            Image(
                painter = rememberImagePainter(weather.iconUrl),
                contentDescription = "Weather icon",
                modifier = Modifier.size(100.dp)
            )
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
            "Clear",
            ""
        )
        WeatherBoard(weather = weather)
    }
}
