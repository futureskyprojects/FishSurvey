package com.blogspot.tndev1403.fishSurvey.Model;

import android.content.Intent;

import com.blogspot.tndev1403.fishSurvey.MainActivity;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.View.fsCategorizeActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsNewUserActivity;

public class MainModel {
    MainActivity mainActivity;
    public fsUser user;

    public MainModel(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        user = new fsUser(mainActivity);
    }
}
