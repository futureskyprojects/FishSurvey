package com.blogspot.tndev1403.fishSurvey.View;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter.fsSavedDataAdapter;
import com.blogspot.tndev1403.fishSurvey.R;

import java.util.ArrayList;

public class fsSavedDataActivity extends AppCompatActivity {
    public static fsElement REVIEW_ELEMENT;
    public static fsCatched REVIEW_CATCHED;
    public static Bitmap CURRENT_BITMAP;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_saved_data);
        recyclerView = (RecyclerView) findViewById(R.id.fssd_recycleView);
        fsCatchedHandler handler = new fsCatchedHandler(this);
        ArrayList<fsCatched> catcheds = handler.getAllEntry();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        fsSavedDataAdapter adapter = new fsSavedDataAdapter(catcheds);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        initToolbar();
    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Xem lại");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
