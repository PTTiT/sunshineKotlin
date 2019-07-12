package com.ttp.sunshine_kotlin.ui.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.ttp.sunshine_kotlin.data.SunshineRepository
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.util.TestUtil
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Created by Franz on 12/25/2017.
 */
class DetailViewModelTest {
    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    lateinit var sunshineRepository: SunshineRepository

    lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        detailViewModel = DetailViewModel(sunshineRepository)
    }

    @Test
    fun setDate() {
        val today = SunshineDateUtils.getNormalizedUtcDateForToday()
        val todayEntry = WeatherEntry(1, today, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val liveData = MutableLiveData<WeatherEntry>()
        liveData.value = todayEntry
        `when`(sunshineRepository.getWeatherByDate(today)).thenReturn(liveData)
        detailViewModel.setDate(today)
        val data = TestUtil.getValue(detailViewModel.mWeather)
        assertThat(data, notNullValue())
        assertThat(data!!.date?.time, `is`(today.time))
    }
}