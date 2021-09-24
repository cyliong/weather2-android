package com.example.ltp.weather2.model

data class Weather(
    val city: String,
    val countryCode: String,
    val temperature: Double,
    val humidity: Int,
    val condition: String
)