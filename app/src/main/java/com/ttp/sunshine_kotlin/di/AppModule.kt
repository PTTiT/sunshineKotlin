package com.ttp.sunshine_kotlin.di

import com.ttp.sunshine_kotlin.data.SunshineRepository
import com.ttp.sunshine_kotlin.data.network.WeatherApi
import com.ttp.sunshine_kotlin.data.network.WeatherConverterFactory
import com.ttp.sunshine_kotlin.data.network.WeatherNetworkDataSource
import com.ttp.sunshine_kotlin.ui.list.ForecastViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Franz on 12/7/2017.
 */
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideNetworkApi(): WeatherApi = Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL).addConverterFactory(WeatherConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

    @Singleton
    @Provides
    fun proviceNetworkDataSource(weatherApi: WeatherApi): WeatherNetworkDataSource = WeatherNetworkDataSource(weatherApi)

    @Singleton
    @Provides
    fun provideSunshineRepository(networkDataSource: WeatherNetworkDataSource): SunshineRepository = SunshineRepository(networkDataSource)

    @Singleton
    @Provides
    fun provideForecastViewModelFactory(sunshineRepository: SunshineRepository): ForecastViewModelFactory = ForecastViewModelFactory(sunshineRepository)
}