package com.si.sunshine;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.si.sunshine.model.WeatherDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private TextView selectedDate;
    private TextView weatherDetailsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        Button selectDateButton = findViewById(R.id.select_date_button);
        selectedDate = findViewById(R.id.selected_date_text);
        weatherDetailsTv = findViewById(R.id.weather_details_text);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int initialYear = c.get(Calendar.YEAR);
        int initialMonthOfYear = c.get(Calendar.MONTH);
        int initialDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        showDate(initialDayOfMonth, initialMonthOfYear, initialYear);

        selectDateButton.setOnClickListener(button -> {
            DatePickerDialog datePickerDialog
                    = new DatePickerDialog(this,
                    (datePicker, year, monthOfYear, dayOfMonth) -> {
                        showDate(dayOfMonth, monthOfYear, year);
                        formatAndFetchData(dayOfMonth, monthOfYear, year);
                    },
                    initialYear, initialMonthOfYear, initialDayOfMonth);
            datePickerDialog.show();
        });
    }

    private void formatAndFetchData(int dayOfMonth, int monthOfYear, int year) {
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
        fetchWeatherDetails(date);
    }

    private void fetchWeatherDetails(long date) {
        JsonWeatherTask task = new JsonWeatherTask();
        task.execute(date);
    }

    private void showDate(int dayOfMonth, int monthOfYear, int year) {
        selectedDate.setText(getResources().getString(R.string.formatted_date, String.valueOf(dayOfMonth), String.valueOf(monthOfYear + 1), String.valueOf(year)));
    }

    private class JsonWeatherTask extends AsyncTask<Long, Void, WeatherDetails> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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

            progressBar.setVisibility(View.GONE);
            weatherDetailsTv.setText(getResources().getString(R.string.formatted_temp_details,
                    String.valueOf(weatherDetails.getMinTemp()),
                    String.valueOf(weatherDetails.getMaxTemp())));
        }
    }
}
