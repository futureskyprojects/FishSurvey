package me.vistark.coppavietnam.View;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import me.vistark.coppavietnam.Presenter.fsSaveSuccessfulPresenter;
import me.vistark.coppavietnam.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;

public class fsSaveSuccessfulActivity extends AppCompatActivity {
    /* Declare views */
//    public ImageView ivPreview;
    public SliderLayout slSlideReview;
    public TextView tvName;
    public TextView tvLength;
    public TextView tvWeight;
    public TextView tvPosition;
    public TextView tvCatchedTime;
    public Button   btnNext;

    /* Declare presenter */
    public fsSaveSuccessfulPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_save_successful);
        initViews();
        initPresenter();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, fsHome.class));
        finish();
        super.onBackPressed();
    }

    private void initPresenter() {
        presenter = new fsSaveSuccessfulPresenter(fsSaveSuccessfulActivity.this);
    }

    private void initViews() {
        sliderInits();
        tvName = (TextView) findViewById(R.id.fss_fish_name);
        tvLength = (TextView) findViewById(R.id.fss_length);
        tvWeight = (TextView) findViewById(R.id.fss_weight);
        tvPosition = (TextView) findViewById(R.id.fss_position);
        tvCatchedTime = (TextView) findViewById(R.id.fss_time);
        btnNext = (Button) findViewById(R.id.fss_next_btn);
    }

    private void sliderInits() {
        slSlideReview = (SliderLayout) findViewById(R.id.imageSlider);
        slSlideReview.setAutoScrolling(false);
        slSlideReview.setIndicatorAnimation(IndicatorAnimations.FILL);
    }
}
