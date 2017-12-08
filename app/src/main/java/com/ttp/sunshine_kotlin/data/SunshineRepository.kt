package com.ttp.sunshine_kotlin.data

import android.arch.lifecycle.LiveData
import android.util.Log
import com.ttp.sunshine_kotlin.data.db.SunshineDao
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.data.network.WeatherNetworkDataSource
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.*

/**
 * Created by Franz on 11/28/2017.
 */
class SunshineRepository(weatherNetworkDataSource: WeatherNetworkDataSource, sunshineDao: SunshineDao) {
    private val LOG_TAG: String? = SunshineRepository::class.simpleName
    private var mWeatherNetworkDataSource = weatherNetworkDataSource
    private var mSunshineDao = sunshineDao
    private var isInitialized = false

    companion object {
        @Volatile private var INSTANCE: SunshineRepository? = null

        fun getInstance(weatherNetworkDataSource: WeatherNetworkDataSource, sunshineDao: SunshineDao): SunshineRepository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: SunshineRepository(weatherNetworkDataSource, sunshineDao).also { INSTANCE = it }
                }

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
                            mSunshineDao.deleteOldData(SunshineDateUtils.getNormalizedUtcDateForToday())
                            Log.d(LOG_TAG, "Insert ${it.size} entries")
                            mSunshineDao.insert(it)
                        }
                    }
                }
            }
        }
    }

    fun getWeatherForecast(date: Date): LiveData<Array<WeatherEntry>> {
        initializeData()
        mWeatherNetworkDataSource.fetchWeather()
        return mSunshineDao.getWeatherForecastByDate(date)
    }
}