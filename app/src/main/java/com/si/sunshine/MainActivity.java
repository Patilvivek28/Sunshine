package com.si.sunshine;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
    private CardView weatherDetailsCard;
    private TextView minTempTv;
    private TextView maxTempTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        Button selectDateButton = findViewById(R.id.select_date_button);
        selectedDate = findViewById(R.id.selected_date_text);
        weatherDetailsCard = findViewById(R.id.weather_details_card);
        minTempTv = findViewById(R.id.min_temp_value);
        maxTempTv = findViewById(R.id.max_temp_value);

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
                    },
                    initialYear, initialMonthOfYear, initialDayOfMonth);
            datePickerDialog.show();
        });
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

    private void showDate(int dayOfMonth, int monthOfYear, int year) {
        weatherDetailsCard.setVisibility(View.GONE);
        selectedDate.setText(getResources().getString(R.string.formatted_date, String.valueOf(dayOfMonth), String.valueOf(monthOfYear + 1), String.valueOf(year)));

        if (isPrime(dayOfMonth)) {
            fetchWeatherDetails(dayOfMonth, monthOfYear, year);
        }
    }

    public boolean isPrime(int num) {
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
            weatherDetailsCard.setVisibility(View.VISIBLE);
            minTempTv.setText(getResources().getString(R.string.formatted_temp_value, String.valueOf(weatherDetails.getMinTemp())));
            maxTempTv.setText(getResources().getString(R.string.formatted_temp_value, String.valueOf(weatherDetails.getMaxTemp())));
        }
    }
}
