package com.blogspot.tndev1403.fishSurvey.view.storagesceen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.model.FishCatch;
import com.blogspot.tndev1403.fishSurvey.model.Fish;
import com.blogspot.tndev1403.fishSurvey.data.db.FishCatchHandler;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenPresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenActivity;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class StorageScreenActivity extends AppCompatActivity {
    public static Fish REVIEW_ELEMENT;
    public static FishCatch REVIEW_CATCHED;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_saved_data);
        CheckRequestPermission();
        recyclerView = (RecyclerView) findViewById(R.id.fssd_recycleView);
        FishCatchHandler handler = new FishCatchHandler(this);
        ArrayList<FishCatch> catcheds = handler.get(HomeScreenPresenter.CURRENT_TRIP_ID);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        StorageScreenAdapter adapter = new StorageScreenAdapter(catcheds, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        initToolbar();
    }

    private void CheckRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Global.PERMISSION.READ_EXTERNAL_STORAGE);
                return;
            }
        }
    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Xem lại");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeScreenActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Global.PERMISSION.READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(this, "Cấp quyền đọc thành công!", Toast.LENGTH_SHORT, true);
            } else {
                Toasty.error(this, "Cấp quyền đọc không thành công!", Toast.LENGTH_SHORT, true);
            }
        }
    }
}