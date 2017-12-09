package com.ttp.sunshine_kotlin.data.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by Franz on 11/28/2017.
 */
class WeatherConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return WeatherJsonConverter
    }

    object WeatherJsonConverter : Converter<ResponseBody, WeatherResponse> {
        private val mParser: OpenWeatherJsonParser = OpenWeatherJsonParser()

//        companion object {
//            @Volatile private var INSTANCE: WeatherJsonConverter? = null
//
//            fun getInstance(): WeatherJsonConverter =
//                    INSTANCE ?: synchronized(this) {
//                        INSTANCE ?: WeatherJsonConverter().also { INSTANCE = it }
//                    }
//        }

        override fun convert(value: ResponseBody?): WeatherResponse? {
            return mParser.parse(value?.string())
        }

    }
}