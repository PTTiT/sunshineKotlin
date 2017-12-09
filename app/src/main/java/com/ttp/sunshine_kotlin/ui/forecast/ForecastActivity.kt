package com.ttp.sunshine_kotlin.ui.forecast

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.Toast
import com.ttp.sunshine_kotlin.R
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.ui.detail.DetailActivity
import java.util.*
import javax.inject.Inject

class ForecastActivity : AppCompatActivity(), ForecastAdapter.ForecastAdapterOnItemClickHandler {
    private lateinit var mForecastAdapter: ForecastAdapter
    private var mRecycleView: RecyclerView? = null
    private var mProgressBar: ProgressBar? = null

    @Inject
    lateinit var mViewModelFactory: ForecastViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        mRecycleView = findViewById(R.id.recyclerview_forecast)
        mProgressBar = findViewById(R.id.pb_loading_indicator)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        mForecastAdapter = ForecastAdapter(this, this)
        mRecycleView?.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = mForecastAdapter
        }

        val forecastViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ForecastViewModel::class.java)

        forecastViewModel.mWeatherForecast.observe(this, Observer<Array<WeatherEntry>> { weatherForecast ->
            weatherForecast?.asList()?.let {
                mForecastAdapter.swapForecast(it)
                mForecastAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onItemClick(date: Date) {
        val timestamp = date.time
        val detailIntent = Intent(this, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_TIMESTAMP, timestamp)
        startActivity(detailIntent)
    }

}
