package com.blogspot.tndev1403.fishSurvey;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {
    /* Declare view here */
    public ProgressBar prgCircle;
    public ProgressBar prgHorizontial;
    public TextView tvStatusText;
    public LinearLayout lnlayoutInitGroup;
    /* Declare presenter */
    public MainPresenter mainPresenter;


    public void UserData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lnlayoutInitGroup.setVisibility(View.GONE);
                prgCircle.setVisibility(View.VISIBLE);
            }
        });
    }

    public void initData(final int MaxProgressbar) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prgHorizontial.setIndeterminate(false);
                prgHorizontial.setMax(MaxProgressbar);
                tvStatusText.setText("");
                lnlayoutInitGroup.setVisibility(View.VISIBLE);
                prgCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationConfig.LANGUAGE.InitLanguage(this);
        initPermission();
        initViews();
        initPresenter();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ApplicationConfig.PERMISSION.ALL_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void initViews() {
        prgCircle = (ProgressBar) findViewById(R.id.home_progress_bar);
        prgHorizontial = (ProgressBar) findViewById(R.id.home_init_progress_bar);
        tvStatusText = (TextView) findViewById(R.id.home_init_text);
        lnlayoutInitGroup = (LinearLayout) findViewById(R.id.home_init_group);
    }

    private void initPresenter() {
        mainPresenter = new MainPresenter(MainActivity.this);
    }
}
