package me.vistark.coppavietnam.View;

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

import me.vistark.coppavietnam.Model.Config.ApplicationConfig;
import me.vistark.coppavietnam.Model.Entity.fsCatched;
import me.vistark.coppavietnam.Model.Entity.fsElement;
import me.vistark.coppavietnam.Model.fsCatchedHandler;
import me.vistark.coppavietnam.Presenter.fsAdapter.fsSavedDataAdapter;
import me.vistark.coppavietnam.Presenter.fsHomePresenter;
import me.vistark.coppavietnam.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class fsSavedDataActivity extends AppCompatActivity {
    public static fsElement REVIEW_ELEMENT;
    public static fsCatched REVIEW_CATCHED;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_saved_data);
        CheckRequestPermission();
        recyclerView = (RecyclerView) findViewById(R.id.fssd_recycleView);
        fsCatchedHandler handler = new fsCatchedHandler(this);
        ArrayList<fsCatched> catcheds = handler.getAllEntry(fsHomePresenter.CURRENT_TRIP_ID);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        fsSavedDataAdapter adapter = new fsSavedDataAdapter(catcheds, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        initToolbar();
    }

    private void CheckRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ApplicationConfig.PERMISSION.READ_EXTERNAL_STORAGE);
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
        startActivity(new Intent(this, fsHome.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ApplicationConfig.PERMISSION.READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(this, "Cấp quyền đọc thành công!", Toast.LENGTH_SHORT, true);
            } else {
                Toasty.error(this, "Cấp quyền đọc không thành công!", Toast.LENGTH_SHORT, true);
            }
        }
    }
}
