package com.ttp.sunshine_kotlin.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.data.network.WeatherResponse
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.mock.Calls
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Franz on 12/15/2017.
 */
object TestUtil {
    fun <T, L : LiveData<T>> getValue(liveData: L): T? {
        val data: Array<Any?> = arrayOfNulls(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)
            }
        }

        liveData.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        return data[0] as T
    }

    fun <T> getResponse(call: Call<T>): T? {
        val latch = CountDownLatch(1)
        var data: T? = null
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>?, t: Throwable?) {
                latch.countDown()
            }

            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                data = response?.body()
                latch.countDown()
            }
        })
        latch.await(10, TimeUnit.SECONDS)
        return data
    }

    fun <T> successCall(data: T): Call<T> {
        return Calls.response(data)
    }

    fun createTestWeatherResponse(size: Int, startDateMs: Long): WeatherResponse {
        val weatherEntries = Array(size, {
            WeatherEntry(it, Date(startDateMs + it * SunshineDateUtils.DAY_IN_MILLIS), it * 1.0, it * 1.0, it * 1.0, it * 1.0, it * 1.0, it * 1.0)
        })
        return WeatherResponse(weatherEntries)
    }
}