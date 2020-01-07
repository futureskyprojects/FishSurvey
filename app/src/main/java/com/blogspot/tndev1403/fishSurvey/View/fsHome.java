package com.blogspot.tndev1403.fishSurvey.View;

import android.os.Bundle;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Presenter.fsHomePresenter;
import com.blogspot.tndev1403.fishSurvey.R;

import java.util.ArrayList;

public class fsHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /* Declare global view */
    public Button btnOtherCategorizes;
    public Toolbar toolbar;
    public ArrayList<ImageView> categozies;
    public DrawerLayout drawer;

    public ImageView ivEditProfile;
    public LinearLayout lnReview;
    public LinearLayout lnEndTrip;
    public ImageView ivVietnamese;
    public ImageView ivEnglish;
    public TextView tvUsername;
    public TextView tvPhone;
    public TextView tvBoatCode;
    public TextView tvPreviewCount;
    public LinearLayout lnAllFamilies;
    public TextView tvNoTrips;
    public TextView tvEndTripText;
    public TextView tvNotSyncShow;
    public LinearLayout lnNoTripLayout;

    /* Declare presenter */
    fsHomePresenter presenter;
    public static int FishList[] = {R.drawable.f1, R.drawable.f2, R.drawable.f3, R.drawable.f4, R.drawable.f5, R.drawable.f6, R.drawable.f7, R.drawable.ic_question};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initToolbar();
        initView();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        if (presenter.tmUpdate != null)
            presenter.tmUpdate.cancel();
        super.onDestroy();
    }

    private void initPresenter() {
        presenter = new fsHomePresenter(fsHome.this);
    }

    private void initToolbar() {
        getSupportActionBar().setTitle(R.string.choose_family);
    }

    private void initView() {
        btnOtherCategorizes = (Button) findViewById(R.id.fsc_other_btn);
        categozies = new ArrayList<>();
        categozies.add((ImageView) findViewById(R.id.fsc_g1));
        categozies.add((ImageView) findViewById(R.id.fsc_g2));
        categozies.add((ImageView) findViewById(R.id.fsc_g3));
        categozies.add((ImageView) findViewById(R.id.fsc_g4));
        categozies.add((ImageView) findViewById(R.id.fsc_g5));
        categozies.add((ImageView) findViewById(R.id.fsc_g6));
        categozies.add((ImageView) findViewById(R.id.fsc_g7));
        categozies.add((ImageView) findViewById(R.id.fsc_g8));
        //--------------------------
        ivEditProfile = (ImageView) findViewById(R.id.edit_profile);
        lnReview = (LinearLayout) findViewById(R.id.review);
        lnEndTrip = (LinearLayout) findViewById(R.id.end_trip);
        ivVietnamese = (ImageView) findViewById(R.id.vn);
        ivEnglish = (ImageView) findViewById(R.id.en);
        tvUsername = (TextView) findViewById(R.id.fsh_fisherman_name);
        tvPhone = (TextView) findViewById(R.id.fsh_phone_number);
        tvBoatCode = (TextView) findViewById(R.id.fsh_boat_code);
        tvPreviewCount = (TextView) findViewById(R.id.preview_count);
        lnAllFamilies = (LinearLayout) findViewById(R.id.all_families);
        tvNoTrips = (TextView) findViewById(R.id.no_trip_show);
        tvEndTripText = (TextView) findViewById(R.id.end_trip_txt);
        tvNotSyncShow = (TextView) findViewById(R.id.not_sync);
        lnNoTripLayout = (LinearLayout) findViewById(R.id.no_trip_layout);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void CreateEndTripButton() {
        lnAllFamilies.setVisibility(View.VISIBLE);
        lnNoTripLayout.setVisibility(View.INVISIBLE);
        tvNoTrips.setVisibility(View.INVISIBLE);
        lnEndTrip.setBackground(getResources().getDrawable(R.drawable.background_danger));
        tvEndTripText.setText(R.string.end_trip);
    }

    public void CreateNewTripButton() {
        lnAllFamilies.setVisibility(View.INVISIBLE);
        lnNoTripLayout.setVisibility(View.VISIBLE);
        tvNoTrips.setVisibility(View.VISIBLE);
        lnEndTrip.setBackground(getResources().getDrawable(R.drawable.background_blue));
        tvEndTripText.setText(R.string.new_trip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fs_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
