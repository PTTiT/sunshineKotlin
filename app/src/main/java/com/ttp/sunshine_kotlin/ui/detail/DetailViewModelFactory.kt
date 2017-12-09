package com.ttp.sunshine_kotlin.ui.detail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ttp.sunshine_kotlin.data.SunshineRepository
import java.util.*

/**
 * Created by Franz on 12/9/2017.
 */
class DetailViewModelFactory(private var mSunshineRepository: SunshineRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(mSunshineRepository) as T
    }
}