package com.ttp.sunshine_kotlin.utilities

import com.ttp.sunshine_kotlin.data.SunshineRepository
import com.ttp.sunshine_kotlin.data.network.WeatherApi
import com.ttp.sunshine_kotlin.data.network.WeatherConverterFactory
import com.ttp.sunshine_kotlin.data.network.WeatherNetworkDataSource
import com.ttp.sunshine_kotlin.ui.list.ForecastViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Franz on 11/30/2017.
 */
class InjectorUtils {
    companion object {
        private var mWeatherApi: WeatherApi? = null

        fun provideNetworkApi(): WeatherApi {
            if (mWeatherApi == null) {
                mWeatherApi = Retrofit.Builder()
                        .baseUrl(WeatherApi.BASE_URL)
                        .addConverterFactory(WeatherConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(WeatherApi::class.java)
            }
            return mWeatherApi!!
        }

        fun provideRepository(): SunshineRepository {
            val networkDataSource = provideNetworkDataSource()
            return SunshineRepository.getInstance(networkDataSource)
        }

        fun provideNetworkDataSource(): WeatherNetworkDataSource {
            val weatherApi = provideNetworkApi()
            return WeatherNetworkDataSource.getInstance(weatherApi)
        }

        fun provideMainActivityViewModelFactory(): ForecastViewModelFactory {
            val repository = provideRepository()
            return ForecastViewModelFactory(repository)
        }
    }

}