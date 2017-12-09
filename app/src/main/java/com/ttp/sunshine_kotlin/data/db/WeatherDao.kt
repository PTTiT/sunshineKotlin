package com.ttp.sunshine_kotlin.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import java.util.*

/**
 * Created by Franz on 12/8/2017.
 */
@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entries: Array<WeatherEntry>)

    @Query("SELECT * FROM weather WHERE date >= :date")
    fun getWeatherForecastByDate(date: Date): LiveData<Array<WeatherEntry>>

    @Query("DELETE FROM weather WHERE date < :date")
    fun deleteOldData(date: Date)

    @Query("SELECT count(id) FROM weather where date >= :date")
    fun countAllFutureForecast(date: Date): Int

    @Query("SELECT * FROM weather WHERE date = :date")
    fun getWeatherByDate(date: Date): LiveData<WeatherEntry>
}