package com.ttp.sunshine_kotlin.data.db

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Created by Franz on 12/8/2017.
 */
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? = date?.time
}