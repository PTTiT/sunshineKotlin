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
import java.util.*
import javax.inject.Inject

class ForecastActivity : AppCompatActivity(), ForecastAdapter.ForecastAdapterOnItemClickHandler {
    private lateinit var mForecastAdapter: ForecastAdapter
    var mRecycleView: RecyclerView? = null
    var mProgressBar: ProgressBar? = null

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

        val mForecastViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ForecastViewModel::class.java)

        mForecastViewModel.mWeatherForecast.observe(this, Observer<List<WeatherEntry>> { weatherForecast ->
            weatherForecast?.let {
                mForecastAdapter.swapForecast(it)
                mForecastAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onItemClick(date: Date) {
        Toast.makeText(this, date.toString(), Toast.LENGTH_LONG).show()
    }

}
