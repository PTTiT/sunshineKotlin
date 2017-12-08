package com.ttp.sunshine_kotlin.di

import android.app.Application
import com.ttp.sunshine_kotlin.SunshineKotlinApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Franz on 12/8/2017.
 */
@Singleton
@Component(modules = [(AppModule::class), (ForecastActivityModule::class), (AndroidInjectionModule::class)])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: SunshineKotlinApplication)
}