package com.si.sunshine.main;

import android.util.Log;

import com.si.sunshine.main.components.JsonWeatherTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class MainPresenter implements MainContract.Presenter, JsonWeatherTask.JsonWeatherTaskListener {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private MainContract.View view;
    private JsonWeatherTask weatherTask;

    MainPresenter(MainContract.View view, JsonWeatherTask weatherTask) {
        this.view = view;
        this.weatherTask = weatherTask;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        weatherTask.setListener(this);
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

        weatherTask = new JsonWeatherTask();
        weatherTask.setListener(this);
        weatherTask.execute(date);
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

    @Override
    public void onPreExecute() {
        view.onPreExecute();
    }

    @Override
    public void onPostExecute(String minTemp, String maxTemp) {
        view.onPostExecute(minTemp, maxTemp);
    }
}
