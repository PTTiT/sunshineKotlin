package com.ttp.sunshine_kotlin.ui.forecast

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.ttp.sunshine_kotlin.data.SunshineRepository
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.util.TestUtil
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Created by Franz on 12/25/2017.
 */
class ForecastViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var sunshineRepository: SunshineRepository

    lateinit var forecastViewModel: ForecastViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetForecast() {
        val liveData = MutableLiveData<Array<WeatherEntry>>()
        val today = SunshineDateUtils.getNormalizedUtcDateForToday()
        val weatherResponse = TestUtil.createTestWeatherResponse(10, today.time)
        `when`(sunshineRepository.getWeatherForecast(today)).thenReturn(liveData)

        forecastViewModel = ForecastViewModel(sunshineRepository)
        liveData.postValue(weatherResponse.mWeatherForecast)
        val data = TestUtil.getValue(forecastViewModel.mWeatherForecast)

        assertThat(data, notNullValue())
        assertThat(data!!.size, `is`(10))
    }
}