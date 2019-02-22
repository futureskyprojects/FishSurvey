package com.blogspot.tndev1403.fishSurvey.Model;

import com.blogspot.tndev1403.fishSurvey.MainActivity;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;

public class MainModel {
    MainActivity mainActivity;
    public fsUser user;

    public MainModel(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        user = new fsUser(mainActivity);
    }
}
