package com.ttp.sunshine_kotlin.ui.list

import android.arch.lifecycle.ViewModel
import com.ttp.sunshine_kotlin.data.SunshineRepository

/**
 * Created by Franz on 11/30/2017.
 */
class ForecastViewModel(mSunshineRepository: SunshineRepository) : ViewModel() {
    var mWeatherForecast = mSunshineRepository.getWeatherForecast()
}