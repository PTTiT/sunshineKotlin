package com.ttp.sunshine_kotlin.data

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.ttp.sunshine_kotlin.data.db.WeatherDao
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.data.network.WeatherApi
import com.ttp.sunshine_kotlin.data.network.WeatherNetworkDataSource
import com.ttp.sunshine_kotlin.data.network.WeatherResponse
import com.ttp.sunshine_kotlin.util.TestUtil
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Created by Franz on 12/22/2017.
 */
class SunshineRepositoryTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var weatherNetworkDataSource: WeatherNetworkDataSource

    @Mock
    lateinit var weatherDao: WeatherDao

    @Mock
    lateinit var weatherApi: WeatherApi

    lateinit var sunshineRespository: SunshineRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherNetworkDataSource = WeatherNetworkDataSource(weatherApi)
        val liveDataForecast = MutableLiveData<Array<WeatherEntry>>()
        weatherNetworkDataSource.mWeatherForecast = liveDataForecast
        sunshineRespository = SunshineRepository(weatherNetworkDataSource, weatherDao)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testGetWeatherForecast() {
        val liveDataForecast = MutableLiveData<Array<WeatherEntry>>()
        val today = SunshineDateUtils.getNormalizedUtcDateForToday()
        `when`(weatherDao.countAllFutureForecast(today)).thenReturn(0)
        `when`(weatherDao.getWeatherForecastByDate(today)).thenReturn(liveDataForecast)

        val todayMs = SunshineDateUtils.getNormalizedUtcMsForToday()
        val mockEntries = Array(5, {
            WeatherEntry(it, Date(todayMs + it * SunshineDateUtils.DAY_IN_MILLIS), it * 1.0, it * 1.0, it * 1.0, it * 1.0, it * 1.0, it * 1.0)
        })
        val mockWeatherResponse = WeatherResponse(mockEntries)
        `when`(weatherApi.weatherForecast(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(TestUtil.successCall(mockWeatherResponse))

        val data = TestUtil.getValue(sunshineRespository.getWeatherForecast(today))

//        val data = TestUtil.getValue(weatherNetworkDataSource.mWeatherForecast)
        assertThat(data, notNullValue())
        assertThat(data!!.size, `is`(5))

//        verify(weatherDao).insert(mockEntries)
    }
}