package com.si.sunshine.main.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDetails {

    private float minTemp;
    private float maxTemp;

    public float getMinTemp() {
        return minTemp;
    }

    private void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    private void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public static WeatherDetails getWeather(String data) {
        WeatherDetails weatherDetails = new WeatherDetails();

        Log.d("WeatherDetails: ", data);

        try {
            JSONObject object = new JSONObject(data);
            JSONObject main = object.getJSONObject("daily");
            JSONArray dataArray = main.getJSONArray("data");
            JSONObject jsonObject = (JSONObject) dataArray.get(0);
            weatherDetails.setMinTemp((float)jsonObject.getDouble("temperatureLow"));
            weatherDetails.setMaxTemp((float)jsonObject.getDouble("temperatureHigh"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherDetails;
    }
}
