package com.blogspot.tndev1403.fishSurvey.view.fishcatchinputscreen;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.view.fishscreen.FishScreenPresenter;
import com.blogspot.tndev1403.fishSurvey.view.fishscreen.FishScreenActivity;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenPresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenActivity;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Input fish activity
 */

public class FishCatchInputActivity extends AppCompatActivity {
    /* Declare global view */
    public ImageView imgIhumbnail;
    public ImageButton btnCamera;
    public ImageButton btnGallery;
    public EditText etLength;
    public EditText etWeight;
    public TextView tvClock;
    public TextView tvCalendar;
    public Button btnFinish;
    public RecyclerView rvCatchedListImages;
    public TextView tvAnnoucement;

    /* Declare presenter */
    public FishCatchInputPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_catched_input);

        initView();
        initPresenter();
        initToolbarAndDefaultData();
    }

    private void initPresenter() {
        presenter = new FishCatchInputPresenter(FishCatchInputActivity.this);
    }

    private void initView() {
        this.imgIhumbnail = findViewById(R.id.fsct_thumb_nail);
        this.btnCamera = findViewById(R.id.fsct_camera);
        this.btnGallery = findViewById(R.id.fsct_gallery);
        this.etLength = findViewById(R.id.fsct_length);
        this.etWeight = findViewById(R.id.fsct_weight);
        this.tvClock = findViewById(R.id.fsct_time);
        this.tvCalendar = findViewById(R.id.fsct_date);
        this.btnFinish = findViewById(R.id.fsct_finish);
        this.rvCatchedListImages = findViewById(R.id.list_inp_fish_image);
        this.tvAnnoucement = findViewById(R.id.empty_annoucement);
    }

    private void initToolbarAndDefaultData() {
        String[] Names = FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getName().split(" - ");
        if (Global.CoppaLanguage.getLanguageCode(this).equals("vi")) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(Names[(Names.length >= 2 ? 1 : 0)].trim());
        } else {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(Names[0].trim());
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage() != null)
            imgIhumbnail.setImageBitmap(FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage());
        else
            imgIhumbnail.setImageDrawable(getResources().getDrawable(R.drawable.ic_error_404));
    }


    @Override
    public void onBackPressed() {
        Intent elementIntent;
        if (FishScreenPresenter.CURRENT_SELECTED_ELEMENT.getId() != -1) {
            elementIntent = new Intent(this, FishScreenActivity.class);
            elementIntent.putExtra(Global.CategorizeAPI.ID, HomeScreenPresenter.GID + 1);
            elementIntent.putExtra(Global.CategorizeAPI.Name, "G" + HomeScreenPresenter.GID);
        } else {
            elementIntent = new Intent(this, HomeScreenActivity.class);
        }
        startActivity(elementIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.resultProcessing(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.permissionResultProcessing(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter.mGoogleApiClient != null) {
            presenter.mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        if (presenter.mGoogleApiClient != null) {
            presenter.mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
