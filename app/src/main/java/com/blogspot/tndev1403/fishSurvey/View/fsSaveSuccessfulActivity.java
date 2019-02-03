package com.blogspot.tndev1403.fishSurvey.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Presenter.fsSaveSuccessfulPresenter;
import com.blogspot.tndev1403.fishSurvey.R;

public class fsSaveSuccessfulActivity extends AppCompatActivity {
    /* Declare views */
    public ImageView ivPreview;
    public TextView tvName;
    public TextView tvLength;
    public TextView tvWeight;
    public TextView tvPosition;
    public TextView tvCatchedTime;
    public Button   btnNext;

    /* Declare presenter */
    public fsSaveSuccessfulPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_save_successful);
        initViews();
        initPresenter();
    }

    private void initPresenter() {
        presenter = new fsSaveSuccessfulPresenter(fsSaveSuccessfulActivity.this);
    }

    private void initViews() {
        ivPreview = (ImageView) findViewById(R.id.fss_image_view);
        tvName = (TextView) findViewById(R.id.fss_fish_name);
        tvLength = (TextView) findViewById(R.id.fss_length);
        tvWeight = (TextView) findViewById(R.id.fss_weight);
        tvPosition = (TextView) findViewById(R.id.fss_position);
        tvCatchedTime = (TextView) findViewById(R.id.fss_time);
        btnNext = (Button) findViewById(R.id.fss_next_btn);
    }
}
