package com.si.sunshine.main.components;

import android.os.AsyncTask;

import com.si.sunshine.main.model.WeatherDetails;

public class JsonWeatherTask extends AsyncTask<Long, Void, WeatherDetails> {

    private JsonWeatherTaskListener listener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (listener != null) {
            listener.onPreExecute();
        }
    }

    @Override
    protected WeatherDetails doInBackground(Long... inputParams) {
        WeatherDetails weatherDetails;
        String data = new WeatherHttpClient().getWeatherData(inputParams[0]);

        weatherDetails = WeatherDetails.getWeather(data);

        return weatherDetails;
    }

    @Override
    protected void onPostExecute(WeatherDetails weatherDetails) {
        super.onPostExecute(weatherDetails);

        if (listener != null) {
            listener.onPostExecute(String.valueOf(weatherDetails.getMinTemp()), String.valueOf(weatherDetails.getMaxTemp()));
        }
    }

    public void setListener(JsonWeatherTaskListener listener) {
        this.listener = listener;
    }

    public interface JsonWeatherTaskListener {
        void onPreExecute();

        void onPostExecute(String minTemp, String maxTemp);
    }
}
