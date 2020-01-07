package com.blogspot.tndev1403.fishSurvey.View;

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

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsCatchedInputPresenter;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsElementPresenter;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsHomePresenter;
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
    public RecyclerView rvCatchedListImages;
    public TextView tvAnnoucement;

    /* Declare presenter */
    public fsCatchedInputPresenter presenter;

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
        String[] Names = fsElementPresenter.CURRENT_SELECTED_ELEMENT.getName().split(" - ");
        if (ApplicationConfig.LANGUAGE.GetLanguageCode(this).equals("vi")) {
            getSupportActionBar().setTitle(Names[(Names.length>=2?1:0)].trim());
        } else {
            getSupportActionBar().setTitle(Names[0].trim());
        }

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
        this.rvCatchedListImages = (RecyclerView) findViewById(R.id.list_inp_fish_image);
        this.tvAnnoucement = (TextView) findViewById(R.id.empty_annoucement);
    }

    @Override
    public void onBackPressed() {
        Intent elementIntent;
        if (fsElementPresenter.CURRENT_SELECTED_ELEMENT.getID() != -1) {
            elementIntent = new Intent(this, fsElementActivity.class);
            elementIntent.putExtra(ApplicationConfig.CategorizeAPI.ID, fsHomePresenter.GID  + 1);
            elementIntent.putExtra(ApplicationConfig.CategorizeAPI.Name, "G" + fsHomePresenter.GID);
        } else {
            elementIntent = new Intent(this, fsHome.class);
        }
        startActivity(elementIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
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
        if (presenter.mGoogleApiClient != null)
        {
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
        super.onDestroy();
    }
}
