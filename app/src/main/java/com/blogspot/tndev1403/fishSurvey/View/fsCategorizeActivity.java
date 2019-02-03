package com.blogspot.tndev1403.fishSurvey.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsCategorizePresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.skyfishjy.library.RippleBackground;

import java.util.concurrent.ExecutionException;

public class fsCategorizeActivity extends AppCompatActivity {
    /* Declare global view */
    public Button btnOtherCategorizes;
    public ImageView categozies[];
    /* Declare presenter */
    fsCategorizePresenter presenter;
    public static int FishList[] = {R.drawable.g1, R.drawable.g2, R.drawable.g3, R.drawable.g4, R.drawable.g5, R.drawable.g6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_categorize);
        initView();
        initToolbar();
        initPresenter();
    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Chọn loại (Choose family)");
    }

    private void initPresenter() {
        presenter = new fsCategorizePresenter(fsCategorizeActivity.this);
    }

    private void initView() {
        btnOtherCategorizes = (Button) findViewById(R.id.fsc_other_btn);
        categozies = new ImageView[6];
        categozies[0] = (ImageView) findViewById(R.id.fsc_g1);
        categozies[1] = (ImageView) findViewById(R.id.fsc_g2);
        categozies[2] = (ImageView) findViewById(R.id.fsc_g3);
        categozies[3] = (ImageView) findViewById(R.id.fsc_g4);
        categozies[4] = (ImageView) findViewById(R.id.fsc_g5);
        categozies[5] = (ImageView) findViewById(R.id.fsc_g6);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saved_data) {
            startActivity(new Intent(fsCategorizeActivity.this, fsSavedDataActivity.class));
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
