package com.ttp.sunshine_kotlin.ui.list

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ttp.sunshine_kotlin.data.SunshineRepository

/**
 * Created by Franz on 11/30/2017.
 */
class ForecastViewModelFactory(var mSunshineRepository: SunshineRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForecastActivityViewModel(mSunshineRepository) as T
    }
}