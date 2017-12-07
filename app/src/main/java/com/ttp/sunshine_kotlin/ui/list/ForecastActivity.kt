package com.ttp.sunshine_kotlin.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.Toast
import com.ttp.sunshine_kotlin.R
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.utilities.InjectorUtils
import java.util.*

class ForecastActivity : AppCompatActivity(), ForecastAdapter.ForecastAdapterOnItemClickHandler {
    var mForecastAdapter: ForecastAdapter? = null
    var mRecycleView: RecyclerView? = null
    var mProgressBar: ProgressBar? = null
    var mForecastActivityViewModel: ForecastActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        mRecycleView = findViewById(R.id.recyclerview_forecast)
        mProgressBar = findViewById(R.id.pb_loading_indicator)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecycleView!!.layoutManager = layoutManager
        mRecycleView!!.setHasFixedSize(true)

        mForecastAdapter = ForecastAdapter(this, this)
        mRecycleView!!.adapter = mForecastAdapter

        val viewModelFactory = InjectorUtils.provideMainActivityViewModelFactory()
        mForecastActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(ForecastActivityViewModel::class.java)

        mForecastActivityViewModel!!.mWeatherForecast.observe(this, Observer<List<WeatherEntry>> { weatherForecast ->
            run {
                mForecastAdapter?.swapForecast(weatherForecast!!)
                mForecastAdapter?.notifyDataSetChanged()
            }
        })
    }

    override fun onItemClick(date: Date) {
        Toast.makeText(this, date.toString(), Toast.LENGTH_LONG).show()
    }

}
