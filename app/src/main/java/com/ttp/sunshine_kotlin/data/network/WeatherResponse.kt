package com.ttp.sunshine_kotlin.data.network

import com.ttp.sunshine_kotlin.data.db.WeatherEntry

/**
 * Created by Franz on 11/28/2017.
 */
class WeatherResponse(weatherForecast: Array<WeatherEntry>) {
    val mWeatherForecast = weatherForecast
}