package com.blogspot.tndev1403.fishSurvey.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Presenter.fsSaveSuccessfulPresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

public class fsSaveSuccessfulActivity extends AppCompatActivity {
    /* Declare views */
//    public ImageView ivPreview;
    public SliderLayout slSlideReview;
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, fsHome.class));
        finish();
        super.onBackPressed();
    }

    private void initPresenter() {
        presenter = new fsSaveSuccessfulPresenter(fsSaveSuccessfulActivity.this);
    }

    private void initViews() {
//        ivPreview = (ImageView) findViewById(R.id.fss_image_view);
        sliderInits();
        tvName = (TextView) findViewById(R.id.fss_fish_name);
        tvLength = (TextView) findViewById(R.id.fss_length);
        tvWeight = (TextView) findViewById(R.id.fss_weight);
        tvPosition = (TextView) findViewById(R.id.fss_position);
        tvCatchedTime = (TextView) findViewById(R.id.fss_time);
        btnNext = (Button) findViewById(R.id.fss_next_btn);
    }

    private void sliderInits() {
        slSlideReview = (SliderLayout) findViewById(R.id.imageSlider);
        slSlideReview.setScrollTimeInSec(3); //set scroll delay in seconds :
        slSlideReview.setIndicatorAnimation(IndicatorAnimations.FILL);
    }
}
