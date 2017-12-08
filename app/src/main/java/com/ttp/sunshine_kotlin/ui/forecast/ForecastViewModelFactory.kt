package com.ttp.sunshine_kotlin.ui.forecast

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ttp.sunshine_kotlin.data.SunshineRepository
import javax.inject.Singleton

/**
 * Created by Franz on 11/30/2017.
 */

@Singleton
class ForecastViewModelFactory(var mSunshineRepository: SunshineRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForecastViewModel(mSunshineRepository) as T
    }
}