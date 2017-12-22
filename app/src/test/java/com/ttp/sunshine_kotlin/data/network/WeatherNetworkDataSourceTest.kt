package com.ttp.sunshine_kotlin.data.network

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.ttp.sunshine_kotlin.util.TestUtil
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Franz on 12/21/2017.
 */

@RunWith(JUnit4::class)
class WeatherNetworkDataSourceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var weatherApi: WeatherApi
    lateinit var weatherNetworkDataSource: WeatherNetworkDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherNetworkDataSource = WeatherNetworkDataSource(weatherApi)
    }

    @Test
    fun testFetchWeather() {
        val weatherResponse = TestUtil.createTestWeatherResponse(5, SunshineDateUtils.getNormalizedUtcMsForToday())
        val mockCall = TestUtil.successCall(weatherResponse)
        Mockito.`when`(weatherApi.weatherForecast(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(mockCall)

        weatherNetworkDataSource.fetchWeather()

        val value = TestUtil.getValue(weatherNetworkDataSource.mWeatherForecast)

        assertThat(value, notNullValue())
        assertThat(value!!.size, `is`(5))
    }
}