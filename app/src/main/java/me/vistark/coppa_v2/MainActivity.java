package me.vistark.coppa_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.vistark.coppa_v2.Model.Config.ApplicationConfig;
import me.vistark.coppa_v2.Presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {
    /* Declare view here */
    public ProgressBar prgCircle;
    public ProgressBar prgHorizontial;
    public TextView tvStatusText;
    public LinearLayout lnlayoutInitGroup;
    /* Declare presenter */
    public MainPresenter mainPresenter;


    public void UserData() {
        lnlayoutInitGroup.setVisibility(View.GONE);
        prgCircle.setVisibility(View.VISIBLE);
    }

    public void initData(final int MaxProgressbar) {
        prgHorizontial.setIndeterminate(false);
        prgHorizontial.setMax(MaxProgressbar);
        tvStatusText.setText("");
        lnlayoutInitGroup.setVisibility(View.VISIBLE);
        prgCircle.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationConfig.LANGUAGE.InitLanguage(this);
        initViews();
        initPresenter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ApplicationConfig.PERMISSION.ALL_PERMISSION) {
            if (grantResults.length >= 4)
                mainPresenter.CallCheckInitData();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void initViews() {
        prgCircle = (ProgressBar) findViewById(R.id.home_progress_bar);
        prgHorizontial = (ProgressBar) findViewById(R.id.home_init_progress_bar);
        tvStatusText = (TextView) findViewById(R.id.home_init_text);
        lnlayoutInitGroup = (LinearLayout) findViewById(R.id.home_init_group);
        lnlayoutInitGroup.setVisibility(View.INVISIBLE);
    }

    private void initPresenter() {
        mainPresenter = new MainPresenter(MainActivity.this);
    }
}
