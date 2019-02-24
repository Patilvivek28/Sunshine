package com.si.sunshine.main;

import android.os.AsyncTask;
import android.util.Log;

import com.si.sunshine.main.components.WeatherHttpClient;
import com.si.sunshine.main.model.WeatherDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private MainContract.View view;

    MainPresenter(MainContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        //Do nothing
    }

    private void fetchWeatherDetails(int dayOfMonth, int monthOfYear, int year) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        long date = 0;

        String month = String.valueOf(monthOfYear + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }

        String day = String.valueOf(dayOfMonth);
        if (day.length() == 1) {
            day = "0" + day;
        }

        try {
            date = dateFormat.parse(String.valueOf(year) + month + day).getTime();
        } catch (ParseException e) {
            Log.e(TAG, "ParseException: ", e);
        }
        date = date / 1000;

        JsonWeatherTask task = new JsonWeatherTask();
        task.execute(date);
    }

    @Override
    public void processDate(int dayOfMonth, int monthOfYear, int year) {
        if (isPrime(dayOfMonth)) {
            fetchWeatherDetails(dayOfMonth, monthOfYear, year);
        }
    }

    private boolean isPrime(int num) {
        if (num > 2 && num % 2 == 0) {
            return false;
        }
        int top = (int) Math.sqrt(num) + 1;
        for (int i = 3; i < top; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    private class JsonWeatherTask extends AsyncTask<Long, Void, WeatherDetails> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            view.onPreExecute();
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

            view.onPostExecute(String.valueOf(weatherDetails.getMinTemp()), String.valueOf(weatherDetails.getMaxTemp()));
        }
    }
}
