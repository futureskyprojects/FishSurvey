package com.blogspot.tndev1403.fishSurvey.view.newcaptainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.tndev1403.fishSurvey.R;

public class NewCaptainScreenActivity extends AppCompatActivity {
    /* Declare global view */
    public ImageView header_icon_1; // Icon 1 in header of input/edit captain info screen
    public ImageView header_icon_2; // Icon 2 in header of input/edit captain info screen
    public ImageView header_icon_3; // Icon 3 in header of input/edit captain info screen
    public ImageView logo; // Logo of COPPA in input/edit captain info screen
    public TextView description; // Description below logo in input/edit captain info screen
    public EditText user_name;
    public EditText user_phone;
    public EditText user_boat_code;
    public Button finish_button;
    /* Declare Presenter */
    NewCaptainScreenPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_new_user);
        initView();

        /* Init presenter */
        presenter = new NewCaptainScreenPresenter(NewCaptainScreenActivity.this);
    }

    void initView() {
        header_icon_1 = findViewById(R.id.fsNewUser_header_icon_1);
        header_icon_2 = findViewById(R.id.fsNewUser_header_icon_2);
        header_icon_3 = findViewById(R.id.fsNewUser_header_icon_3);
        logo = findViewById(R.id.fsNewUser_logo);
        description = findViewById(R.id.fsNewUser_description);
        user_name = findViewById(R.id.fsNewUser_username);
        user_phone = findViewById(R.id.fsNewUser_userphone);
        user_boat_code = findViewById(R.id.fsNewUser_userboatcode);
        finish_button = findViewById(R.id.fsNewUser_finish);
    }
}
