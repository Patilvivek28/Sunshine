package com.si.sunshine;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.si.sunshine.model.WeatherDetails;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView selectedDate;
    private TextView weatherDetailsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    (datePicker, year, monthOfYear, dayOfMonth) ->
                            showDate(dayOfMonth, monthOfYear, year),
                    initialYear, initialMonthOfYear, initialDayOfMonth);
            datePickerDialog.show();
        });

        JsonWeatherTask task = new JsonWeatherTask();
        task.execute("Pune,IN");
    }

    private void showDate(int dayOfMonth, int monthOfYear, int year) {
        selectedDate.setText(getResources().getString(R.string.formatted_date, String.valueOf(dayOfMonth), String.valueOf(monthOfYear + 1), String.valueOf(year)));
    }

    private class JsonWeatherTask extends AsyncTask<String, Void, WeatherDetails> {

        @Override
        protected WeatherDetails doInBackground(String... inputParams) {
            WeatherDetails weatherDetails;
            String data = new WeatherHttpClient().getWeatherData(inputParams[0]);

            weatherDetails = WeatherDetails.getWeather(data);

            return weatherDetails;
        }

        @Override
        protected void onPostExecute(WeatherDetails weatherDetails) {
            super.onPostExecute(weatherDetails);

            weatherDetailsTv.setText(weatherDetails.getDate()+"\n"+
                    getResources().getString(R.string.formatted_temp_details,
                    String.valueOf(weatherDetails.getMinTemp()),
                    String.valueOf(weatherDetails.getMaxTemp())));
        }
    }
}
