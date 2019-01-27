package com.blogspot.tndev1403.fishSurvey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blogspot.tndev1403.fishSurvey.Presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {
    MainPresenter mainPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(MainActivity.this);
    }
}
