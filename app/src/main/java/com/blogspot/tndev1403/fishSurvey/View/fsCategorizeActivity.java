package com.blogspot.tndev1403.fishSurvey.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsCategorizePresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;

import java.util.concurrent.ExecutionException;

public class fsCategorizeActivity extends AppCompatActivity {
    /* Declare global view */
    public Button btnOtherCategorizes;
    public GridView gvCategorizes;
    /* Declare presenter */
    fsCategorizePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_categorize);
        initView();
        initPresenter();
    }

    private void initPresenter() {
        presenter = new fsCategorizePresenter(fsCategorizeActivity.this);
    }

    private void initView() {
        btnOtherCategorizes = (Button) findViewById(R.id.fsc_other_btn);
        gvCategorizes = (GridView) findViewById(R.id.fsc_categorizes_view);
    }
}
