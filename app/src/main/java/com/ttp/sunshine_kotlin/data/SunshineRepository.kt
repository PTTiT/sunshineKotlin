package com.ttp.sunshine_kotlin.data

import android.arch.lifecycle.LiveData
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.data.network.WeatherNetworkDataSource

/**
 * Created by Franz on 11/28/2017.
 */
class SunshineRepository(weatherNetworkDataSource: WeatherNetworkDataSource) {
    val LOG_TAG: String? = SunshineRepository::class.simpleName
    var mWeatherNetworkDataSource = weatherNetworkDataSource

    companion object {
        @Volatile private var INSTANCE: SunshineRepository? = null

        fun getInstance(weatherNetworkDataSource: WeatherNetworkDataSource): SunshineRepository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: SunshineRepository(weatherNetworkDataSource).also { INSTANCE = it }
                }

    }

    fun getWeatherForecast(): LiveData<List<WeatherEntry>> {
        mWeatherNetworkDataSource.fetchWeather()
        return mWeatherNetworkDataSource.mWeatherForecast
    }
}