package com.ttp.sunshine_kotlin.ui.forecast

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ttp.sunshine_kotlin.R
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils
import com.ttp.sunshine_kotlin.utilities.SunshineWeatherUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Franz on 11/28/2017.
 */
class ForecastAdapter(context: Context, clickHandler: ForecastAdapterOnItemClickHandler) : RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>() {
    private val VIEW_TYPE_TODAY = 0
    private val VIEW_TYPE_FUTURE_DAY = 1

    val mContext = context
    val mClickHandler = clickHandler
    val mUseTodayLayout = mContext.resources.getBoolean(R.bool.use_today_layout)

    var mForecast: List<WeatherEntry>? = ArrayList()

    fun swapForecast(newForecast: List<WeatherEntry>) {
        mForecast = newForecast
        notifyDataSetChanged()
    }

    private fun getLayoutIdByType(viewType: Int): Int =
            when (viewType) {

                VIEW_TYPE_TODAY -> {
                    R.layout.list_item_forecast_today
                }

                VIEW_TYPE_FUTURE_DAY -> {
                    R.layout.forecast_list_item
                }

                else -> throw IllegalArgumentException("Invalid view type, value of " + viewType)
            }


    override fun getItemViewType(position: Int): Int =
            when {
                mUseTodayLayout && position == 0 -> VIEW_TYPE_TODAY
                else -> VIEW_TYPE_FUTURE_DAY
            }


    private fun getImageResourceId(weatherIconId: Int, position: Int): Int {
        val viewType = getItemViewType(position)

        return when (viewType) {

            VIEW_TYPE_TODAY -> SunshineWeatherUtils
                    .getLargeArtResourceIdForWeatherCondition(weatherIconId)

            VIEW_TYPE_FUTURE_DAY -> SunshineWeatherUtils
                    .getSmallArtResourceIdForWeatherCondition(weatherIconId)

            else -> throw IllegalArgumentException("Invalid view type, value of " + viewType)
        }
    }

    override fun onBindViewHolder(forecastAdapterViewHolder: ForecastAdapterViewHolder?, position: Int) {
        val currentWeather = mForecast!![position]

        /****************
         * Weather Icon *
         ****************/
        val weatherIconId = currentWeather.weatherIconId
        val weatherImageResourceId = getImageResourceId(weatherIconId, position)
        forecastAdapterViewHolder?.iconView?.setImageResource(weatherImageResourceId)

        /****************
         * Weather Date *
         ****************/
        val dateInMillis = currentWeather.date?.time
        /* Get human readable string using our utility method */
        val dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis!!, false)

        /* Display friendly date string */
        forecastAdapterViewHolder?.dateView?.text = dateString

        /***********************
         * Weather Description *
         ***********************/
        val description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherIconId)
        /* Create the accessibility (a11y) String from the weather description */
        val descriptionA11y = mContext.getString(R.string.a11y_forecast, description)

        /* Set the text and content description (for accessibility purposes) */
        forecastAdapterViewHolder?.descriptionView?.text = description
        forecastAdapterViewHolder?.descriptionView?.contentDescription = descriptionA11y

        /**************************
         * High (max) temperature *
         **************************/
        val highInCelsius = currentWeather.max
        /*
          * If the user's preference for weather is fahrenheit, formatTemperature will convert
          * the temperature. This method will also append either °C or °F to the temperature
          * String.
          */
        val highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius)
        /* Create the accessibility (a11y) String from the weather description */
        val highA11y = mContext.getString(R.string.a11y_high_temp, highString)

        /* Set the text and content description (for accessibility purposes) */
        forecastAdapterViewHolder?.highTempView?.text = highString
        forecastAdapterViewHolder?.highTempView?.contentDescription = highA11y

        /*************************
         * Low (min) temperature *
         *************************/
        val lowInCelsius = currentWeather.min
        /*
          * If the user's preference for weather is fahrenheit, formatTemperature will convert
          * the temperature. This method will also append either °C or °F to the temperature
          * String.
          */
        val lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius)
        val lowA11y = mContext.getString(R.string.a11y_low_temp, lowString)

        /* Set the text and content description (for accessibility purposes) */
        forecastAdapterViewHolder?.lowTempView?.text = lowString
        forecastAdapterViewHolder?.lowTempView?.contentDescription = lowA11y
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ForecastAdapterViewHolder {
        val layoutId = getLayoutIdByType(viewType)
        val view = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
        view.isFocusable = true
        return ForecastAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mForecast!!.size
    }

    interface ForecastAdapterOnItemClickHandler {
        fun onItemClick(date: Date)
    }

    inner class ForecastAdapterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val adapterPosition = adapterPosition
            val date = mForecast?.get(adapterPosition)?.date
            mClickHandler.onItemClick(date!!)
        }

        val iconView: ImageView = itemView!!.findViewById(R.id.weather_icon)
        val dateView: TextView = itemView!!.findViewById(R.id.date)
        val descriptionView: TextView = itemView!!.findViewById(R.id.weather_description)
        val highTempView: TextView = itemView!!.findViewById(R.id.high_temperature)
        val lowTempView: TextView = itemView!!.findViewById(R.id.low_temperature)

        init {
            itemView!!.setOnClickListener(this)
        }
    }
}