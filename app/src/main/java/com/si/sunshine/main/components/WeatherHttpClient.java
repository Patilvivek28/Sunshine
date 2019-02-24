package com.si.sunshine.main.components;

import android.util.Log;

import com.si.sunshine.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class WeatherHttpClient {

    private static final String TAG = "WeatherHttpClient";
    private static final String DEFAULT_LAT_LANG = "18.5204,73.8567";

    public String getWeatherData(long dateInUnix) {

        String result = null;
        int resCode;
        InputStream in;
        try {
            URL url = new URL(BuildConfig.BASE_URL
                    + DEFAULT_LAT_LANG
                    + ","
                    + String.valueOf(dateInUnix)
                    + BuildConfig.ADDITIONAL_DATE_PARAMS);
            URLConnection urlConn = url.openConnection();

            HttpsURLConnection httpsConn = (HttpsURLConnection) urlConn;
            httpsConn.setAllowUserInteraction(false);
            httpsConn.setInstanceFollowRedirects(true);
            httpsConn.setRequestMethod("GET");
            httpsConn.connect();
            resCode = httpsConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpsConn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                in.close();
                result = sb.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: ", e);
        }
        return result;
    }
}
