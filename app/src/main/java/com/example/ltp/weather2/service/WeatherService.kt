package com.example.ltp.weather2.service

import android.util.Log
import com.example.ltp.weather2.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val API_KEY = ""
private const val SERVICE_URL =
    "https://api.openweathermap.org/data/2.5/weather?appid=$API_KEY&units=metric"
private const val ICON_BASE_URL = "https://openweathermap.org/img/wn/"
private const val ICON_FILE_EXTENSION = "@2x.png"
private const val TAG = "WeatherService"

class WeatherService {

    suspend fun getWeather(cityName: String): Weather? = withContext(Dispatchers.IO) {
        try {
            val jsonObject = getServiceResponse(cityName)

            val mainObject = jsonObject.getJSONObject("main")
            val temperature = mainObject.getDouble("temp")
            val humidity = mainObject.getInt("humidity")

            val weatherObject = jsonObject.getJSONArray("weather").getJSONObject(0)
            val condition = weatherObject.getString("main")
            val icon = weatherObject.getString("icon")
            val iconUrl = ICON_BASE_URL + icon + ICON_FILE_EXTENSION

            val city = jsonObject.getString("name")

            val systemObject = jsonObject.getJSONObject("sys")
            val countryCode = systemObject.getString("country")

            Log.v(
                TAG,
                "Weather - $city, $countryCode: $temperature°C, $humidity%, $condition, $iconUrl."
            )

            return@withContext Weather(city, countryCode, temperature, humidity, condition, iconUrl)
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        }
        return@withContext null
    }

    private fun getServiceResponse(cityName: String): JSONObject {
        val urlString = "$SERVICE_URL&q=$cityName"
        Log.v(TAG, "URL: $urlString")

        val url = URL(urlString)
        val connection = url.openConnection() as HttpsURLConnection

        var data = connection.inputStream.read()
        val builder = StringBuilder()

        while (data != -1) {
            builder.append(data.toChar())
            data = connection.inputStream.read()
        }
        val json = builder.toString()
        Log.v(TAG, "JSON: $json")
        return JSONObject(json)
    }

}