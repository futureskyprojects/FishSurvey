package com.blogspot.tndev1403.fishSurvey.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.Presenter.fsElementPresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.skyfishjy.library.RippleBackground;

public class fsElementActivity extends AppCompatActivity {
    /* Declare global view */
    public GridView gridView;
    public RippleBackground bottomEffect;
    public ImageView bottomArrow;
    /* Declare presenter */
    fsElementPresenter presenter;

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
//        getSupportActionBar().setTitle(presenter.CATEGORIZE_NAME);
        getSupportActionBar().setTitle("Chọn loài (Choose fish)");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
//        else if (item.getItemId() == R.id.saved_data) {
//            startActivity(new Intent(fsElementActivity.this, fsSavedDataActivity.class));
//        }
        return false;
    }

    private void initPresenter() {
        presenter = new fsElementPresenter(fsElementActivity.this);
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.fse_elements_view);
        bottomEffect = (RippleBackground) findViewById(R.id.fsc_btn_bottom);
        bottomEffect.startRippleAnimation();
        bottomArrow = (ImageView) findViewById(R.id.fsc_btn_bottom_arrow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
