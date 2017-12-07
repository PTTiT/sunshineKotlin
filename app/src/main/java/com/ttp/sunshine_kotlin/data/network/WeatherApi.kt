package com.ttp.sunshine_kotlin.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Franz on 11/28/2017.
 */
interface WeatherApi {
    companion object {
        val BASE_URL = "https://andfun-weather.udacity.com/"
    }

    @GET("weather?mode=json&units=metric")
    fun weatherForecast(@Query("q") location: String, @Query("cnt") numDays: Int): Call<WeatherResponse>
}