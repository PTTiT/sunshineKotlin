package com.ttp.sunshine_kotlin.utilities

import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ttp.sunshine_kotlin.R
import java.util.*

/**
 * Created by Franz on 12/9/2017.
 */
@BindingAdapter("largeWeatherIconId")
fun bindLargeWeatherIcon(view: ImageView, weatherIconId: Int) {
    val resId = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherIconId)
    view.setImageResource(resId)
}

@BindingAdapter("smallWeatherIconId")
fun bindSmallWeatherIcon(view: ImageView, weatherIconId: Int) {
    val resId = SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherIconId)
    view.setImageResource(resId)
}

@BindingAdapter("weatherDescription")
fun bindDescription(view: TextView, weatherId: Int) {
    view.text = SunshineWeatherUtils.getStringForWeatherCondition(view.context, weatherId)
}

@BindingAdapter("weatherDate")
fun bindDate(view: TextView, date: Date?) {
    view.text = SunshineDateUtils.getFriendlyDateString(view.context, date?.time ?: 0, true)
}

@BindingAdapter("weatherHumidity")
fun bindHumidity(view: TextView, humidity: Double) {
    view.text = view.context.getString(R.string.format_humidity, humidity)
}

@BindingAdapter("weatherPressure")
fun bindPressure(view: TextView, pressure: Double) {
    view.text = view.context.getString(R.string.format_pressure, pressure)
}

@BindingAdapter(value = [("weatherWind"), ("weatherDegrees")])
fun bindWind(view: TextView, windSpeed: Double, windDirection: Double) {
    view.text = SunshineWeatherUtils.getFormattedWind(view.context, windSpeed, windDirection)
}