package com.blogspot.tndev1403.fishSurvey.view.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.data.config.Global;

public class SplashScreenActivity extends AppCompatActivity {
    /* Declare view */
    public ProgressBar prgCircle;
    public ProgressBar prgHorizontial;
    public TextView tvStatusText;
    public LinearLayout lnlayoutInitGroup;
    /* Declare presenter */
    public SplashScreenPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Global.CoppaLanguage.initLanguage(this);
        initViews();
        initPresenter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Global.PERMISSION.ALL_PERMISSION) {
            if (grantResults.length >= 4)
                presenter.CallCheckInitData();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void initViews() {
        prgCircle = findViewById(R.id.home_progress_bar);
        prgHorizontial = findViewById(R.id.home_init_progress_bar);
        tvStatusText = findViewById(R.id.home_init_text);
        lnlayoutInitGroup = findViewById(R.id.home_init_group);
        hideInitView();
    }

    private void initPresenter() {
        presenter = new SplashScreenPresenter(SplashScreenActivity.this);
    }

    public void hideInitView() {
        lnlayoutInitGroup.setVisibility(View.GONE);
        prgCircle.setVisibility(View.VISIBLE);
    }

    public void showInitView(final int MaxProgressbar) {
        prgHorizontial.setIndeterminate(false);
        prgHorizontial.setMax(MaxProgressbar);
        tvStatusText.setText("");
        lnlayoutInitGroup.setVisibility(View.VISIBLE);
        prgCircle.setVisibility(View.INVISIBLE);
    }

}
