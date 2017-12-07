package com.ttp.sunshine_kotlin.data.db

import java.util.*

/**
 * Created by Franz on 11/28/2017.
 */
class WeatherEntry {
    var weatherIconId: Int = 0
    var date: Date? = null
    var min: Double = 0.toDouble()
    var max: Double = 0.toDouble()
    var humidity: Double = 0.toDouble()
    var pressure: Double = 0.toDouble()
    var wind: Double = 0.toDouble()
    var degrees: Double = 0.toDouble()

    constructor(weatherIconId: Int, date: Date?, min: Double, max: Double, humidity: Double, pressure: Double, wind: Double, degrees: Double) {
        this.weatherIconId = weatherIconId
        this.date = date
        this.min = min
        this.max = max
        this.humidity = humidity
        this.pressure = pressure
        this.wind = wind
        this.degrees = degrees
    }
}