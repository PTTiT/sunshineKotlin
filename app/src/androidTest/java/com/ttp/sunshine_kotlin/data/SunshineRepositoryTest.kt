package com.ttp.sunshine_kotlin.data

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.ttp.sunshine_kotlin.data.db.WeatherDao
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.data.network.WeatherApi
import com.ttp.sunshine_kotlin.data.network.WeatherNetworkDataSource
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

/**
 * Created by Franz on 12/22/2017.
 */
class SunshineRepositoryTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var weatherApi: WeatherApi

    lateinit var weatherNetworkDataSource: WeatherNetworkDataSource

    @Mock
    lateinit var weatherDao: WeatherDao

    lateinit var sunshineRespository: SunshineRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherNetworkDataSource = WeatherNetworkDataSource(weatherApi)
        sunshineRespository = SunshineRepository(weatherNetworkDataSource, weatherDao)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testGetWeatherForecast() {
        val roomData = MutableLiveData<Array<WeatherEntry>>()
        val today = SunshineDateUtils.getNormalizedUtcDateForToday()
        val mockWeatherResponse = TestUtil.createTestWeatherResponse(10, today.time)
        `when`(weatherDao.countAllFutureForecast(today)).thenReturn(0)
        `when`(weatherDao.getWeatherForecastByDate(today)).thenReturn(roomData)
        `when`(weatherApi.weatherForecast(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(TestUtil.successCall(mockWeatherResponse))

        `when`(weatherNetworkDataSource.fetchWeather()).thenReturn(ArgumentMatchers.any())


        val data = TestUtil.getValue(weatherNetworkDataSource.mWeatherForecast)

        assertThat(data, notNullValue())
        assertThat(data!!.size, `is`(10))
        roomData.postValue(data)

        val newData = TestUtil.getValue(sunshineRespository.getWeatherForecast(today))

        assertThat(newData, notNullValue())
        assertThat(newData!!.size, `is`(10))
    }

    @Test
    fun testGetWeatherByDate() {
        val roomData = MutableLiveData<WeatherEntry>()
        val today = SunshineDateUtils.getNormalizedUtcDateForToday()
        val todayWeatherEntry = WeatherEntry(1, today, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        `when`(weatherDao.getWeatherByDate(today)).thenReturn(roomData)
        roomData.value = todayWeatherEntry

        val data = TestUtil.getValue(sunshineRespository.getWeatherByDate(today))
        assertThat(data, notNullValue())
        assertThat(data!!.date?.time, `is`(today.time))
    }
}