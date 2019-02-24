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

class WeatherHttpClient {

    private static final String TAG = "WeatherHttpClient";

    String getWeatherData(String location) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(BuildConfig.GET_DATA_URL)).openConnection();
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
