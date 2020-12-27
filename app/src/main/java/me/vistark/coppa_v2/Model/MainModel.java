package me.vistark.coppa_v2.Model;

import me.vistark.coppa_v2.MainActivity;
import me.vistark.coppa_v2.Model.Entity.fsUser;

public class MainModel {
    MainActivity mainActivity;
    public fsUser user;

    public MainModel(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        user = new fsUser(mainActivity);
    }
}
