/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttp.sunshine_kotlin.data.network;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ttp.sunshine_kotlin.data.db.WeatherEntry;
import com.ttp.sunshine_kotlin.utilities.SunshineDateUtils;

import java.net.HttpURLConnection;
import java.util.Date;

/**
 * Parser for OpenWeatherMap JSON data.
 */
public final class OpenWeatherJsonParser {

    // Weather information. Each day's forecast info is an element of the "list" array
    private static final String OWM_LIST = "list";

    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WIND_SPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";

    // All temperatures are children of the "temp" object
    private static final String OWM_TEMPERATURE = "temp";

    // Max temperature for the day
    private static final String OWM_MAX = "max";
    private static final String OWM_MIN = "min";

    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_ID = "id";

    private static final String OWM_MESSAGE_CODE = "cod";

    private static boolean hasHttpError(JsonObject forecastJson) {
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.get(OWM_MESSAGE_CODE).getAsInt();

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    return false;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Location invalid
                default:
                    // Server probably down
                    return true;
            }
        }
        return false;
    }

    private static WeatherEntry[] fromJson(final JsonObject forecastJson) {
        JsonArray jsonWeatherArray = forecastJson.get(OWM_LIST).getAsJsonArray();

        WeatherEntry[] weatherEntries = new WeatherEntry[jsonWeatherArray.size()];

        /*
         * OWM returns daily forecasts based upon the local time of the city that is being asked
         * for, which means that we need to know the GMT offset to translate this data properly.
         * Since this data is also sent in-order and the first day is always the current day, we're
         * going to take advantage of that to get a nice normalized UTC date for all of our weather.
         */
        long normalizedUtcStartDay = SunshineDateUtils.getNormalizedUtcMsForToday();

        for (int i = 0; i < jsonWeatherArray.size(); i++) {
            // Get the JSON object representing the day
            JsonObject dayForecast = jsonWeatherArray.get(i).getAsJsonObject();

            // Create the weather entry object
            long dateTimeMillis = normalizedUtcStartDay + SunshineDateUtils.Companion.getDAY_IN_MILLIS() * i;
            WeatherEntry weather = fromJson(dayForecast, dateTimeMillis);

            weatherEntries[i] = weather;
        }
        return weatherEntries;
    }

    private static WeatherEntry fromJson(final JsonObject dayForecast,
                                         long dateTimeMillis) {
        // We ignore all the datetime values embedded in the JSON and assume that
        // the values are returned in-order by day (which is not guaranteed to be correct).

        double pressure = dayForecast.get(OWM_PRESSURE).getAsDouble();
        int humidity = dayForecast.get(OWM_HUMIDITY).getAsInt();
        double windSpeed = dayForecast.get(OWM_WIND_SPEED).getAsDouble();
        double windDirection = dayForecast.get(OWM_WIND_DIRECTION).getAsDouble();


        // Description is in a child array called "weather", which is 1 element long.
        // That element also contains a weather code.
        JsonObject weatherObject =
                dayForecast.get(OWM_WEATHER).getAsJsonArray().get(0).getAsJsonObject();

        int weatherId = weatherObject.get(OWM_WEATHER_ID).getAsInt();


        //  Temperatures are sent by Open Weather Map in a child object called "temp".
        JsonObject temperatureObject = dayForecast.get(OWM_TEMPERATURE).getAsJsonObject();
        double max = temperatureObject.get(OWM_MAX).getAsDouble();
        double min = temperatureObject.get(OWM_MIN).getAsDouble();

        // Create the weather entry object
        return new WeatherEntry(weatherId, new Date(dateTimeMillis), max, min,
                humidity, pressure, windSpeed, windDirection);
    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     * @param forecastJsonStr JSON response from server
     * @return Array of Strings describing weather data
     */
    @Nullable
    public WeatherResponse parse(final String forecastJsonStr) {
        Gson gson = new Gson();
        JsonObject forecastJson = gson.fromJson(forecastJsonStr, JsonObject.class);

        // Is there an error?
        if (hasHttpError(forecastJson)) {
            return null;
        }

        WeatherEntry[] weatherForecast = fromJson(forecastJson);

        return new WeatherResponse(weatherForecast);
    }
}