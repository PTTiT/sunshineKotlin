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
interface SunshineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entries: Array<WeatherEntry>)

    @Query("SELECT * FROM weather WHERE date >= :date")
    fun getWeatherForecastByDate(date: Date): LiveData<Array<WeatherEntry>>

    @Query("DELETE FROM weather WHERE date < :date")
    fun deleteOldData(date: Date)
}