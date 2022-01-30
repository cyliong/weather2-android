# weather2-android
This is a simple weather app built with Jetpack Compose, 
showcasing various Android programming techniques, including:
- Layouts using Material Component composables 
  (`Scaffold`, `TopAppBar`, `TextField`)
- State management with `MutableState`, `rememberSaveable` and `Parcelize`
- Show Snackbar using `ScaffoldState.snackbarHostState`
- Network request using `HttpsURLConnection`
- Deserialize JSON response using `JSONObject`
- Display images from the internet using Coil
- Coroutines with `rememberCoroutineScope`
- Composable preview

## Features
- Search by city name
- Display the city's weather condition

## Requirements
- Android Studio Bumblebee | 2021.1.1 or newer
- Android 5.0 (API level 21) or higher
- Kotlin 1.5 or higher

## Setup
1. Get your API key 
   from [OpenWeatherMap](https://openweathermap.org/api).
2. Replace the `API_KEY` constant in `WeatherService.kt` 
   with your API key.