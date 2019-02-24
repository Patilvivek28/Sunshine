package com.si.sunshine.model;

import com.si.sunshine.Utils;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDetails {

    private long date;
    private float minTemp;
    private float maxTemp;

    public String getDate() {
        return Utils.getFormattedDate(date);
    }

    public void setDate(long date) {
        this.date = date;
    }

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

        try {
            JSONObject object = new JSONObject(data);
            weatherDetails.setDate(object.getLong("dt"));
            JSONObject main = object.getJSONObject("main");
            weatherDetails.setMaxTemp((float) main.getDouble("temp_max"));
            weatherDetails.setMinTemp((float) main.getDouble("temp_min"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherDetails;
    }
}
