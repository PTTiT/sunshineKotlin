package com.ttp.sunshine_kotlin.data.network

import com.ttp.sunshine_kotlin.util.TestUtil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets


/**
 * Created by Franz on 12/19/2017.
 */
@RunWith(JUnit4::class)
class WeatherApiTest {
    lateinit var weatherApi: WeatherApi
    lateinit var webServer: MockWebServer

    @Before
    fun setUp() {
        webServer = MockWebServer()
        weatherApi = Retrofit.Builder()
                .baseUrl(WeatherApi.BASE_URL)
                .addConverterFactory(WeatherConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun testWeatherForecast() {
        enqueueResponse("forecast.json")
        val weatherResponse = TestUtil.getResponse(weatherApi.weatherForecast("Mountain View, CA", 14))
        assertThat(weatherResponse, notNullValue())
        assertEquals(14, weatherResponse!!.mWeatherForecast.size)
    }

    private fun enqueueResponse(filename: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader.getResourceAsStream("api-responses/" + filename)
        val bufferSource = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for (key in headers.keys) {
            mockResponse.addHeader(key, headers[key])
        }
        webServer.enqueue(mockResponse.setBody(bufferSource.readString(StandardCharsets.UTF_8)))
    }
}