package com.ttp.sunshine_kotlin.di

import android.arch.persistence.room.Room
import android.content.Context
import com.ttp.sunshine_kotlin.SunshineKotlinApplication
import com.ttp.sunshine_kotlin.data.SunshineRepository
import com.ttp.sunshine_kotlin.data.db.SunshineDatabase
import com.ttp.sunshine_kotlin.data.network.WeatherApi
import com.ttp.sunshine_kotlin.data.network.WeatherConverterFactory
import com.ttp.sunshine_kotlin.data.network.WeatherNetworkDataSource
import com.ttp.sunshine_kotlin.ui.detail.DetailActivity
import com.ttp.sunshine_kotlin.ui.detail.DetailViewModelFactory
import com.ttp.sunshine_kotlin.ui.forecast.ForecastViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

/**
 * Created by Franz on 12/7/2017.
 */
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: SunshineKotlinApplication): Context = application.applicationContext!!

    @Singleton
    @Provides
    fun provideNetworkApi(): WeatherApi = Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL).addConverterFactory(WeatherConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

    @Singleton
    @Provides
    fun provideSunshineDatabase(context: Context): SunshineDatabase = Room.databaseBuilder(context, SunshineDatabase::class.java, SunshineDatabase.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideNetworkDataSource(weatherApi: WeatherApi): WeatherNetworkDataSource = WeatherNetworkDataSource(weatherApi)

    @Singleton
    @Provides
    fun provideSunshineRepository(networkDataSource: WeatherNetworkDataSource, sunshineDatabase: SunshineDatabase): SunshineRepository = SunshineRepository(networkDataSource, sunshineDatabase.weatherDao())

    @Singleton
    @Provides
    fun provideForecastViewModelFactory(sunshineRepository: SunshineRepository): ForecastViewModelFactory = ForecastViewModelFactory(sunshineRepository)

//    @Provides
//    @DetailActivity.ExtraTimestamp
//    fun provideDate(detailActivity: DetailActivity): Date = Date(detailActivity.getExtraTimestamp())

    @Singleton
    @Provides
    fun provideDetailViewModelFactory(sunshineRepository: SunshineRepository): DetailViewModelFactory = DetailViewModelFactory(sunshineRepository)
}