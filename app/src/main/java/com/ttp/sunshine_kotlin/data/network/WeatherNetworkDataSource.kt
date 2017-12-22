package com.ttp.sunshine_kotlin.data.network

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Franz on 11/28/2017.
 */
open class WeatherNetworkDataSource(weatherApi: WeatherApi) {
    companion object {
        val NUM_DAYS: Int = 14
        val LOCATION: String = "Mountain View, CA"

//        @Volatile private var INSTANCE: WeatherNetworkDataSource? = null
//
//        fun getInstance(weatherApi: WeatherApi): WeatherNetworkDataSource =
//                INSTANCE ?: synchronized(this) {
//                    INSTANCE ?: WeatherNetworkDataSource(weatherApi).also { INSTANCE = it }
//                }

    }

    val LOG_TAG: String? = WeatherNetworkDataSource::class.simpleName
    private var mWeatherApi: WeatherApi = weatherApi
    var mWeatherForecast: MutableLiveData<Array<WeatherEntry>> = MutableLiveData()

    fun fetchWeather() {
//        Log.d(LOG_TAG, "Fetch weather started")
        mWeatherApi.weatherForecast(LOCATION, NUM_DAYS).enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
//                Log.e(LOG_TAG, t?.message ?: "Server error", t)
            }

            override fun onResponse(call: Call<WeatherResponse>?, response: Response<WeatherResponse>?) {
                mWeatherForecast.postValue(response?.body()?.mWeatherForecast)
            }
        })
    }
}