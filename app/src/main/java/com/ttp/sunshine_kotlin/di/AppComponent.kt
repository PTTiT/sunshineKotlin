package com.ttp.sunshine_kotlin.di

import com.ttp.sunshine_kotlin.SunshineKotlinApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Franz on 12/8/2017.
 */
@Singleton
@Component(modules = [(AppModule::class), (ForecastActivityModule::class), (AndroidInjectionModule::class), (DetailActivityModule::class)])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: SunshineKotlinApplication): Builder

        fun build(): AppComponent
    }

    fun inject(application: SunshineKotlinApplication)
}