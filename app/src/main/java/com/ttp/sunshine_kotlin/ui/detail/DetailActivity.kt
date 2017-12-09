package com.ttp.sunshine_kotlin.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ttp.sunshine_kotlin.BR
import com.ttp.sunshine_kotlin.R
import com.ttp.sunshine_kotlin.data.db.WeatherEntry
import com.ttp.sunshine_kotlin.databinding.ActivityDetailBinding
import java.util.*
import javax.inject.Inject


/**
 * Created by Franz on 12/9/2017.
 */
class DetailActivity : AppCompatActivity() {
    @Inject
    lateinit var mViewModelFactory: DetailViewModelFactory

    companion object {
        val EXTRA_TIMESTAMP = "EXTRA_TIMESTAMP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val detailViewModel = ViewModelProviders.of(this, mViewModelFactory).get(DetailViewModel::class.java)
        val timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, -1)
        detailViewModel.setDate(Date(timestamp))
        detailViewModel.mWeather.observe(this, Observer<WeatherEntry> { weather ->
            run {
                binding.setVariable(BR.weather, weather)
                binding.executePendingBindings()
            }
        })
    }

}