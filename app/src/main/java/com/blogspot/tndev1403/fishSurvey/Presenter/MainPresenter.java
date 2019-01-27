package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.content.Intent;

import com.blogspot.tndev1403.fishSurvey.MainActivity;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.Model.MainModel;
import com.blogspot.tndev1403.fishSurvey.View.fsCategorizeActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsNewUserActivity;

public class MainPresenter {
    MainActivity mContext;
    MainModel mainModel;
    public MainPresenter(MainActivity mContext) {
        this.mContext = mContext;
        mainModel = new MainModel(mContext);
        fCheckUserInfo();
    }
    public void fCheckUserInfo() {
        if (!mainModel.user.get()) {
            // If info of user not exists or not enough then start acivity for ask new
            mContext.startActivity(new Intent(mContext, fsNewUserActivity.class));
            mContext.finish();
        } else {
            mContext.startActivity(new Intent(mContext, fsCategorizeActivity.class));
            mContext.finish();
        }
    }
}
