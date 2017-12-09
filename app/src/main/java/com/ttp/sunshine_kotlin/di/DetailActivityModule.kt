package com.ttp.sunshine_kotlin.di

import com.ttp.sunshine_kotlin.ui.detail.DetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Franz on 12/9/2017.
 */
@Module
abstract class DetailActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeDetailActivity(): DetailActivity
}