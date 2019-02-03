package com.blogspot.tndev1403.fishSurvey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
        initViews();
        initPresenter();
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
