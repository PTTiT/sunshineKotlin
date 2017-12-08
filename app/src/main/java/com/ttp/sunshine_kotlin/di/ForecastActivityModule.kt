package com.ttp.sunshine_kotlin.di

import com.ttp.sunshine_kotlin.ui.forecast.ForecastActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Franz on 12/7/2017.
 */
@Module
abstract class ForecastActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeForecastActivity(): ForecastActivity
}