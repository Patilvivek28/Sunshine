package com.si.sunshine.main;

import com.si.sunshine.BasePresenter;
import com.si.sunshine.BaseView;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void onPreExecute();

        void onPostExecute(String minTemp, String maxTemp);
    }

    interface Presenter extends BasePresenter {

        void processDate(int dayOfMonth, int monthOfYear, int year);
    }
}
