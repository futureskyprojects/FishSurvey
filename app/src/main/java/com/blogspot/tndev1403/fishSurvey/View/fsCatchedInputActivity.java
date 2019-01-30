package com.blogspot.tndev1403.fishSurvey.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsCatchedInputPresenter;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsElementPresenter;
import com.blogspot.tndev1403.fishSurvey.R;

public class fsCatchedInputActivity extends AppCompatActivity {
    public final static String TAG = fsCatchedInputActivity.class.getSimpleName();
    /* Declare global view */
    public ImageView imgIhumbnail;
    public ImageButton btnCamera;
    public ImageButton btnGallery;
    public EditText etLength;
    public EditText etWeight;
    public TextView tvClock;
    public TextView tvCalendar;
    public Button btnFinish;

    /* Declare presenter */
    fsCatchedInputPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_catched_input);
        initView();
        /* initialize presenter */
        initPresenter();
        initToolbarAndDefaultData();
    }

    private void initToolbarAndDefaultData() {
        /* For toolbar */
        getSupportActionBar().setTitle(fsElementPresenter.CURRENT_SELECTED_ELEMENT.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /* For other data */
        if (fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage() != null)
            imgIhumbnail.setImageBitmap(fsElementPresenter.CURRENT_SELECTED_ELEMENT.getFeatureImage());
        else
            imgIhumbnail.setImageDrawable(getResources().getDrawable(R.drawable.ic_error_404));
    }

    private void initPresenter() {
        presenter = new fsCatchedInputPresenter(fsCatchedInputActivity.this);
    }

    private void initView() {
        this.imgIhumbnail = (ImageView) findViewById(R.id.fsct_thumb_nail);
        this.btnCamera = (ImageButton) findViewById(R.id.fsct_camera);
        this.btnGallery = (ImageButton) findViewById(R.id.fsct_gallery);
        this.etLength = (EditText) findViewById(R.id.fsct_length);
        this.etWeight = (EditText) findViewById(R.id.fsct_weight);
        this.tvClock = (TextView) findViewById(R.id.fsct_time);
        this.tvCalendar = (TextView) findViewById(R.id.fsct_date);
        this.btnFinish = (Button) findViewById(R.id.fsct_finish);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
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
        if (presenter.mGoogleApiClient != null)
        {
            Log.d(TAG, "onStart: Không null");
            presenter.mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        if (presenter.mGoogleApiClient != null)
        {
            presenter.mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (presenter.mGoogleApiClient != null
                && presenter.mGoogleApiClient.isConnected()) {
            presenter.stopLocationUpdates();
            presenter.mGoogleApiClient.disconnect();
            presenter.mGoogleApiClient = null;
        }
        Log.d(TAG, "onDestroy LocationService");
        super.onDestroy();
    }
}
