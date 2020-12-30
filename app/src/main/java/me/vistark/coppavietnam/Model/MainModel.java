package me.vistark.coppavietnam.Model;

import me.vistark.coppavietnam.MainActivity;
import me.vistark.coppavietnam.Model.Entity.fsUser;

public class MainModel {
    MainActivity mainActivity;
    public fsUser user;

    public MainModel(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        user = new fsUser(mainActivity);
    }
}
