package com.si.sunshine.main;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.si.sunshine.R;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private ProgressBar progressBar;
    private Button selectDateButton;
    private TextView selectedDate;
    private CardView weatherDetailsCard;
    private TextView minTempTv;
    private TextView maxTempTv;

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        new MainPresenter(this);
        presenter.start();

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int initialYear = c.get(Calendar.YEAR);
        int initialMonthOfYear = c.get(Calendar.MONTH);
        int initialDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        showDate(initialDayOfMonth, initialMonthOfYear, initialYear);

        selectDateButton.setOnClickListener(button -> {
            DatePickerDialog datePickerDialog
                    = new DatePickerDialog(this,
                    (datePicker, year, monthOfYear, dayOfMonth) -> showDate(dayOfMonth, monthOfYear, year),
                    initialYear, initialMonthOfYear, initialDayOfMonth);
            datePickerDialog.show();
        });
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progress_bar);
        selectDateButton = findViewById(R.id.select_date_button);
        selectedDate = findViewById(R.id.selected_date_text);
        weatherDetailsCard = findViewById(R.id.weather_details_card);
        minTempTv = findViewById(R.id.min_temp_value);
        maxTempTv = findViewById(R.id.max_temp_value);
    }

    private void showDate(int dayOfMonth, int monthOfYear, int year) {
        weatherDetailsCard.setVisibility(View.GONE);
        selectedDate.setText(getResources().getString(R.string.formatted_date, String.valueOf(dayOfMonth), String.valueOf(monthOfYear + 1), String.valueOf(year)));

        presenter.processDate(dayOfMonth, monthOfYear, year);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(String minTemp, String maxTemp) {
        progressBar.setVisibility(View.GONE);
        weatherDetailsCard.setVisibility(View.VISIBLE);
        minTempTv.setText(getResources().getString(R.string.formatted_temp_value, minTemp));
        maxTempTv.setText(getResources().getString(R.string.formatted_temp_value, maxTemp));
    }
}