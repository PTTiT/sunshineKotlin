package com.ttp.sunshine_kotlin.data

import android.arch.lifecycle.LiveData
import android.util.Log
import com.ttp.sunshine_kotlin.data.db.WeatherDao
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.data.network.WeatherNetworkDataSource
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.*

/**
 * Created by Franz on 11/28/2017.
 */
class SunshineRepository(weatherNetworkDataSource: WeatherNetworkDataSource, weatherDao: WeatherDao) {
    private val LOG_TAG: String? = SunshineRepository::class.simpleName

    private var mWeatherNetworkDataSource = weatherNetworkDataSource
    private var mWeatherDao = weatherDao
    private var isInitialized = false

    companion object {
//        @Volatile private var INSTANCE: SunshineRepository? = null
//
//        fun getInstance(weatherNetworkDataSource: WeatherNetworkDataSource, weatherDao: WeatherDao): SunshineRepository =
//                INSTANCE ?: synchronized(this) {
//                    INSTANCE ?: SunshineRepository(weatherNetworkDataSource, weatherDao).also { INSTANCE = it }
//                }


    }

    private fun initializeData() {
        when (isInitialized) {
            true -> return
            false -> {
                Log.d(LOG_TAG, "Initialize data")
                isInitialized = true

                mWeatherNetworkDataSource.mWeatherForecast.observeForever { t ->
                    t?.let {
                        bg {
                            Log.d(LOG_TAG, "Delete old data")
                            mWeatherDao.deleteOldData(SunshineDateUtils.getNormalizedUtcDateForToday())
                            Log.d(LOG_TAG, "Insert ${it.size} entries")
                            mWeatherDao.insert(it)
                        }
                    }
                }
            }
        }
    }

    fun getWeatherForecast(date: Date): LiveData<Array<WeatherEntry>> {
        initializeData()
        bg {
            val count = mWeatherDao.countAllFutureForecast(SunshineDateUtils.getNormalizedUtcDateForToday())
            if (count < WeatherNetworkDataSource.NUM_DAYS) {
                mWeatherNetworkDataSource.fetchWeather()
            }
        }
        return mWeatherDao.getWeatherForecastByDate(date)
    }

    fun getWeatherByDate(date: Date): LiveData<WeatherEntry> {
        return mWeatherDao.getWeatherByDate(date)
    }
}