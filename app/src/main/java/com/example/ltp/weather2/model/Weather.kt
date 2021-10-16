package com.example.ltp.weather2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val city: String,
    val countryCode: String,
    val temperature: Double,
    val humidity: Int,
    val condition: String,
    val iconUrl: String
) : Parcelable