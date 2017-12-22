package com.ttp.sunshine_kotlin.data.db

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.ttp.sunshine_kotlin.util.TestUtil
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import java.util.*


/**
 * Created by Franz on 12/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {
    lateinit var mSunshineDatabase: SunshineDatabase
    lateinit var mWeatherDao: WeatherDao

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val context = InstrumentationRegistry.getTargetContext()

        mSunshineDatabase = Room.inMemoryDatabaseBuilder(context, SunshineDatabase::class.java).build()
        mWeatherDao = mSunshineDatabase.weatherDao()
    }

    @After
    fun tearDown() {
        mSunshineDatabase.close()
    }

    @Test
    fun testInsertAndCountFutureForecast() {
        val today = SunshineDateUtils.getNormalizedUtcDateForToday()
        val tomorrow = Date(SunshineDateUtils.getNormalizedUtcMsForToday() + SunshineDateUtils.DAY_IN_MILLIS)
        val yesterday = Date(SunshineDateUtils.getNormalizedUtcMsForToday() - SunshineDateUtils.DAY_IN_MILLIS)
        val entryToday = WeatherEntry(1, today, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entryTomorrow = WeatherEntry(2, tomorrow, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entryYesterday = WeatherEntry(3, yesterday, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entries = arrayOf(entryYesterday, entryToday, entryTomorrow)

//        val newObserver: Observer<Array<WeatherEntry>> = mock(Observer<Array<WeatherEntry>>::class.java)

        var count = mWeatherDao.countAllFutureForecast(today)
        assertEquals(0, count)

        mWeatherDao.insert(entries)

        count = mWeatherDao.countAllFutureForecast(today)
        assertEquals(2, count)
    }

    @Test
    fun testGetWeatherForecastByDate() {
        val today = SunshineDateUtils.getNormalizedUtcDateForToday()
        val tomorrow = Date(SunshineDateUtils.getNormalizedUtcMsForToday() + SunshineDateUtils.DAY_IN_MILLIS)
        val yesterday = Date(SunshineDateUtils.getNormalizedUtcMsForToday() - SunshineDateUtils.DAY_IN_MILLIS)
        val entryToday = WeatherEntry(1, today, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entryTomorrow = WeatherEntry(2, tomorrow, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entryYesterday = WeatherEntry(3, yesterday, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entries = arrayOf(entryYesterday, entryToday, entryTomorrow)

        mWeatherDao.insert(entries)

        val forecast = TestUtil.getValue(mWeatherDao.getWeatherForecastByDate(today))
        assertThat(forecast, notNullValue())
        assertEquals(2, forecast!!.size)
        val firstEntry = forecast[0]
        val secondEntry = forecast[1]

        assertThat(firstEntry, notNullValue())
        assertThat(secondEntry, notNullValue())

        assertEquals(entryToday.date!!.time, firstEntry.date!!.time)
        assertEquals(entryTomorrow.date!!.time, secondEntry.date!!.time)
    }

    @Test
    fun testGetWeatherByDate() {
        val today = SunshineDateUtils.getNormalizedUtcDateForToday()
        val tomorrow = Date(SunshineDateUtils.getNormalizedUtcMsForToday() + SunshineDateUtils.DAY_IN_MILLIS)
        val yesterday = Date(SunshineDateUtils.getNormalizedUtcMsForToday() - SunshineDateUtils.DAY_IN_MILLIS)
        val entryToday = WeatherEntry(1, today, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entryTomorrow = WeatherEntry(2, tomorrow, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entryYesterday = WeatherEntry(3, yesterday, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val entries = arrayOf(entryYesterday, entryToday, entryTomorrow)

        mWeatherDao.insert(entries)

        val weather = TestUtil.getValue(mWeatherDao.getWeatherByDate(today))
        assertThat(weather, notNullValue())
        assertEquals(weather!!.date!!.time, today.time)
    }
}