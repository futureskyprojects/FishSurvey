package com.blogspot.tndev1403.fishSurvey.view.fishscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenActivity;
import com.skyfishjy.library.RippleBackground;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Fish screen, show when user select a defined fish category in home screen.
 * in here, user can select determine fish and input caught info about that fish
 */

public class FishScreenActivity extends AppCompatActivity {
    /* Declare global view */
    public GridView gridView;
    public RippleBackground bottomEffect;
    public ImageView bottomArrow;
    /* Declare presenter */
    FishScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_element);
        initView();
        /* Must be end */
        initPresenter();
        // Apply data from presenter to toolbar - must be after presenter initialize
        applyToolbar();
    }

    private void applyToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.choose_fish);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeScreenActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void initPresenter() {
        presenter = new FishScreenPresenter(FishScreenActivity.this);
    }

    private void initView() {
        gridView = findViewById(R.id.fse_elements_view);
        bottomEffect = findViewById(R.id.fsc_btn_bottom);
        bottomEffect.startRippleAnimation();
        bottomArrow = findViewById(R.id.fsc_btn_bottom_arrow);
    }

}
