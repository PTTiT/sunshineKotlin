package com.ttp.sunshine_kotlin.utilities

import android.content.Context
import android.text.format.DateUtils
import com.ttp.sunshine_kotlin.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Franz on 11/28/2017.
 */
class SunshineDateUtils {
    companion object {
        @JvmStatic
        val DAY_IN_MILLIS: Long = TimeUnit.DAYS.toMillis(1)

        @JvmStatic
        fun getNormalizedUtcMsForToday(): Long {
            val nowInMillis = System.currentTimeMillis()
            val timezone = TimeZone.getDefault()
            val gmtOffsetMillis = timezone.getOffset(nowInMillis)
            val timeSinceEpochLocalTimeMillis = nowInMillis + gmtOffsetMillis
            val daysSinceEpochLocal = TimeUnit.MILLISECONDS.toDays(timeSinceEpochLocalTimeMillis)
            return TimeUnit.DAYS.toMillis(daysSinceEpochLocal)
        }

        @JvmStatic
        fun getNormalizedUtcDateForToday(): Date {
            return Date(getNormalizedUtcMsForToday())
        }

        private fun getLocalMidnightFromNormalizedUtcDate(normalizedUtcDate: Long): Long {
            /* The timeZone object will provide us the current user's time zone offset */
            val timeZone = TimeZone.getDefault()
            /*
         * This offset, in milliseconds, when added to a UTC date time, will produce the local
         * time.
         */
            val gmtOffset = timeZone.getOffset(normalizedUtcDate).toLong()
            return normalizedUtcDate - gmtOffset
        }

        private fun elapsedDaysSinceEpoch(utcDate: Long): Long {
            return TimeUnit.MILLISECONDS.toDays(utcDate)
        }

        private fun getReadableDateString(context: Context, timeInMillis: Long): String {
            val flags = (DateUtils.FORMAT_SHOW_DATE
                    or DateUtils.FORMAT_NO_YEAR
                    or DateUtils.FORMAT_SHOW_WEEKDAY)

            return DateUtils.formatDateTime(context, timeInMillis, flags)
        }

        private fun getDayName(context: Context, dateInMillis: Long): String {
            /*
         * If the date is today, return the localized version of "Today" instead of the actual
         * day name.
         */
            val daysFromEpochToProvidedDate = elapsedDaysSinceEpoch(dateInMillis)
            val daysFromEpochToToday = elapsedDaysSinceEpoch(System.currentTimeMillis())

            val daysAfterToday = (daysFromEpochToProvidedDate - daysFromEpochToToday).toInt()

            when (daysAfterToday) {
                0 -> return context.getString(R.string.today)
                1 -> return context.getString(R.string.tomorrow)

                else -> {
                    val dayFormat = SimpleDateFormat("EEEE")
                    return dayFormat.format(dateInMillis)
                }
            }
        }

        @JvmStatic
        fun getFriendlyDateString(context: Context, normalizedUtcMidnight: Long, showFullDate: Boolean): String {

            /*
         * NOTE: localDate should be localDateMidnightMillis and should be straight from the
         * database
         *
         * Since we normalized the date when we inserted it into the database, we need to take
         * that normalized date and produce a date (in UTC time) that represents the local time
         * zone at midnight.
         */
            val localDate = getLocalMidnightFromNormalizedUtcDate(normalizedUtcMidnight)

            /*
         * In order to determine which day of the week we are creating a date string for, we need
         * to compare the number of days that have passed since the epoch (January 1, 1970 at
         * 00:00 GMT)
         */
            val daysFromEpochToProvidedDate = elapsedDaysSinceEpoch(localDate)

            /*
         * As a basis for comparison, we use the number of days that have passed from the epoch
         * until today.
         */
            val daysFromEpochToToday = elapsedDaysSinceEpoch(System.currentTimeMillis())

            if (daysFromEpochToProvidedDate == daysFromEpochToToday || showFullDate) {
                /*
             * If the date we're building the String for is today's date, the format
             * is "Today, June 24"
             */
                val dayName = getDayName(context, localDate)
                val readableDate = getReadableDateString(context, localDate)
                if (daysFromEpochToProvidedDate - daysFromEpochToToday < 2) {
                    /*
                 * Since there is no localized format that returns "Today" or "Tomorrow" in the API
                 * levels we have to support, we take the name of the day (from SimpleDateFormat)
                 * and use it to replace the date from DateUtils. This isn't guaranteed to work,
                 * but our testing so far has been conclusively positive.
                 *
                 * For information on a simpler API to use (on API > 18), please check out the
                 * documentation on DateFormat#getBestDateTimePattern(Locale, String)
                 * https://developer.android.com/reference/android/text/format/DateFormat.html#getBestDateTimePattern
                 */
                    val localizedDayName = SimpleDateFormat("EEEE").format(localDate)
                    return readableDate.replace(localizedDayName, dayName)
                } else {
                    return readableDate
                }
            } else if (daysFromEpochToProvidedDate < daysFromEpochToToday + 7) {
                /* If the input date is less than a week in the future, just return the day name. */
                return getDayName(context, localDate)
            } else {
                val flags = (DateUtils.FORMAT_SHOW_DATE
                        or DateUtils.FORMAT_NO_YEAR
                        or DateUtils.FORMAT_ABBREV_ALL
                        or DateUtils.FORMAT_SHOW_WEEKDAY)

                return DateUtils.formatDateTime(context, localDate, flags)
            }
        }
    }
}