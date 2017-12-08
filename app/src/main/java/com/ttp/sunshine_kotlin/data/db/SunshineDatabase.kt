package com.ttp.sunshine_kotlin.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

/**
 * Created by Franz on 12/8/2017.
 */

@Database(entities = [(WeatherEntry::class)], version = SunshineDatabase.DATABASE_VERSION)
@TypeConverters(DateConverter::class)
abstract class SunshineDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME: String = "sunshine"
        const val DATABASE_VERSION: Int = 1
    }

    abstract fun sunshineDao(): SunshineDao
}