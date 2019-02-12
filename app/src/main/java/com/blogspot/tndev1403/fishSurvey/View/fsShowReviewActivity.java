package com.blogspot.tndev1403.fishSurvey.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Presenter.fsShowReviewPresenter;
import com.blogspot.tndev1403.fishSurvey.R;

public class fsShowReviewActivity extends AppCompatActivity {
    /* Declare views */
    public ImageView ivPreview;
    public TextView tvName;
    public TextView tvLength;
    public TextView tvWeight;
    public TextView tvPosition;
    public TextView tvCatchedTime;
    public Button btnClose;
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
        getSupportActionBar().setTitle(fsSavedDataActivity.REVIEW_ELEMENT.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        ivPreview = (ImageView) findViewById(R.id.fssr_image_view);
        tvName = (TextView) findViewById(R.id.fssr_fish_name);
        tvLength = (TextView) findViewById(R.id.fssr_length);
        tvWeight = (TextView) findViewById(R.id.fssr_weight);
        tvPosition = (TextView) findViewById(R.id.fssr_position);
        tvCatchedTime = (TextView) findViewById(R.id.fssr_time);
        btnClose = (Button) findViewById(R.id.fssr_close_btn);
    }
}
