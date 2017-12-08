package com.ttp.sunshine_kotlin.ui.forecast

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.ttp.sunshine_kotlin.data.SunshineRepository
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils

/**
 * Created by Franz on 11/30/2017.
 */
class ForecastViewModel(mSunshineRepository: SunshineRepository) : ViewModel() {
    var mWeatherForecast: LiveData<Array<WeatherEntry>>

    init {
        val date = SunshineDateUtils.getNormalizedUtcDateForToday()
        mWeatherForecast = mSunshineRepository.getWeatherForecast(date)
    }
}