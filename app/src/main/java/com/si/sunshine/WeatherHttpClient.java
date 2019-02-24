package com.si.sunshine;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherHttpClient {

    private static String TAG = "WeatherHttpClient";
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String UNITS_KEY = "&units=";
    private static String UNITS_VALUE = "metric";
    private static String APP_ID_KEY = "&APPID=";


    public String getWeatherData(String location) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(BASE_URL + location
                    + UNITS_KEY + UNITS_VALUE
                    + APP_ID_KEY + BuildConfig.OPEN_WEATHER_MAP_API_KEY)
            ).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            //Let's read the response
            StringBuilder builder = new StringBuilder();
            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\r\n");
            }
            inputStream.close();
            connection.disconnect();
            return builder.toString();
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: ", e);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: ", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException: ", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException: ", e);
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }
}
