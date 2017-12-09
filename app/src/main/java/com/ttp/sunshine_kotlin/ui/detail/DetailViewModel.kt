package com.ttp.sunshine_kotlin.ui.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.ttp.sunshine_kotlin.data.SunshineRepository
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import java.util.*

/**
 * Created by Franz on 12/9/2017.
 */
class DetailViewModel(private var mSunshineRepository: SunshineRepository) : ViewModel() {
    lateinit var mWeather: LiveData<WeatherEntry>

//    init {
//        val date = SunshineDateUtils.getNormalizedUtcDateForToday()
//        mWeather = mSunshineRepository.getWeatherByDate(date)
//    }

    fun setDate(date: Date) {
        mWeather = mSunshineRepository.getWeatherByDate(date)
    }
}