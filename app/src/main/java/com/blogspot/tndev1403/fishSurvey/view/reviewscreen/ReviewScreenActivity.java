package com.blogspot.tndev1403.fishSurvey.view.reviewscreen;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.storagesceen.StorageScreenActivity;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;

public class ReviewScreenActivity extends AppCompatActivity {
    /* Declare views */
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
    ReviewScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_show_review);
        initViews();
        initPresenter();
        initToolbar();
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            if (StorageScreenActivity.REVIEW_ELEMENT == null)
                getSupportActionBar().setTitle("Other families - Các loài khác");
            else
                getSupportActionBar().setTitle(StorageScreenActivity.REVIEW_ELEMENT.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, StorageScreenActivity.class));
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    private void initPresenter() {
        presenter = new ReviewScreenPresenter(this);
    }

    private void initViews() {
        sliderInits();
        tvName = findViewById(R.id.fssr_fish_name);
        tvLength = findViewById(R.id.fssr_length);
        tvWeight = findViewById(R.id.fssr_weight);
        tvPosition = findViewById(R.id.fssr_position);
        tvCatchedTime = findViewById(R.id.fssr_time);
        btnClose = findViewById(R.id.fssr_close_btn);
        btnDelete = findViewById(R.id.fssr_delete_btn);
        btnEdit = findViewById(R.id.fssr_edit_btn);
    }

    private void sliderInits() {
        slSlideReview = findViewById(R.id.imageSlider2);
        slSlideReview.setScrollTimeInSec(3); //set scroll delay in seconds :
        slSlideReview.setIndicatorAnimation(IndicatorAnimations.FILL);
    }
}
