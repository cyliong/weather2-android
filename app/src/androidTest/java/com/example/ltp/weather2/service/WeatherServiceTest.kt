package com.example.ltp.weather2.service

import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class WeatherServiceTest {

    private lateinit var weatherService: WeatherService

    @Before
    fun setup() {
        weatherService = WeatherService()
    }

    @Test
    fun getWeather() {
        val weather = weatherService.getWeather("Singapore")
        assertNotNull(weather)
    }

}