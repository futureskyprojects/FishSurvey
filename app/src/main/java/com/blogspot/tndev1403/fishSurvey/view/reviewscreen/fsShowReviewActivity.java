package com.blogspot.tndev1403.fishSurvey.view.reviewscreen;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.storagesceen.fsSavedDataActivity;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;

public class fsShowReviewActivity extends AppCompatActivity {
    /* Declare views */
//    public ImageView ivPreview;
    public SliderLayout slSlideReview;
    public TextView tvName;
    public TextView tvLength;
    public TextView tvWeight;
    public TextView tvPosition;
    public TextView tvCatchedTime;
    public Button btnClose;
    public Button btnDelete;
    public Button btnEdit;
    /* Declare presenter */
    fsShowReviewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_show_review);
        initViews();
        initPresenter();
        initToolbar();
    }

    private void initToolbar() {
        if (fsSavedDataActivity.REVIEW_ELEMENT == null)
            getSupportActionBar().setTitle("Other families - Các loài khác");
        else
            getSupportActionBar().setTitle(fsSavedDataActivity.REVIEW_ELEMENT.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, fsSavedDataActivity.class));
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    private void initPresenter() {
        presenter = new fsShowReviewPresenter(this);
    }

    private void initViews() {
//        ivPreview = (ImageView) findViewById(R.id.fssr_image_view);\
        sliderInits();
        tvName = (TextView) findViewById(R.id.fssr_fish_name);
        tvLength = (TextView) findViewById(R.id.fssr_length);
        tvWeight = (TextView) findViewById(R.id.fssr_weight);
        tvPosition = (TextView) findViewById(R.id.fssr_position);
        tvCatchedTime = (TextView) findViewById(R.id.fssr_time);
        btnClose = (Button) findViewById(R.id.fssr_close_btn);
        btnDelete = (Button) findViewById(R.id.fssr_delete_btn);
        btnEdit = (Button) findViewById(R.id.fssr_edit_btn);
    }
    private void sliderInits() {
        slSlideReview = (SliderLayout) findViewById(R.id.imageSlider2);
        slSlideReview.setScrollTimeInSec(3); //set scroll delay in seconds :
        slSlideReview.setIndicatorAnimation(IndicatorAnimations.FILL);
    }
}
