package me.vistark.coppavietnam.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import me.vistark.coppavietnam.Presenter.fsNewUserPresenter;
import me.vistark.coppavietnam.R;

public class fsNewUserActivity extends AppCompatActivity {
    /* Declare global view */
    public ImageView header_icon_1;
    public ImageView header_icon_2;
    public ImageView header_icon_3;
    public ImageView logo;
    public TextView description;
    public EditText user_name;
    public EditText user_phone;
    public EditText user_boat_code;
    public Button finish_button;
    /* Declare Presenter */
    fsNewUserPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_new_user);
        initView();

        /* Init presenter */
        presenter = new fsNewUserPresenter(fsNewUserActivity.this);
    }

    void initView() {
        header_icon_1 = (ImageView) findViewById(R.id.fsNewUser_header_icon_1);
        header_icon_2 = (ImageView) findViewById(R.id.fsNewUser_header_icon_2);
        header_icon_3 = (ImageView) findViewById(R.id.fsNewUser_header_icon_3);
        logo = (ImageView) findViewById(R.id.fsNewUser_logo);
        description = (TextView) findViewById(R.id.fsNewUser_description);
        user_name = (EditText) findViewById(R.id.fsNewUser_username);
        user_phone = (EditText) findViewById(R.id.fsNewUser_userphone);
        user_boat_code = (EditText) findViewById(R.id.fsNewUser_userboatcode);
        finish_button = (Button) findViewById(R.id.fsNewUser_finish);
    }
}
