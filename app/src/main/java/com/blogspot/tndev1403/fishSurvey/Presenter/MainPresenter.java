package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.content.Intent;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.MainActivity;
import com.blogspot.tndev1403.fishSurvey.Model.Config.FishSurveyData;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.Model.MainModel;
import com.blogspot.tndev1403.fishSurvey.Model.fsElementHandler;
import com.blogspot.tndev1403.fishSurvey.View.fsCategorizeActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsNewUserActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import es.dmoral.toasty.Toasty;

public class MainPresenter {
    MainActivity mContext;
    MainModel mainModel;
    fsElementHandler handler;
    FishSurveyData data;
    int position = 0;
    int count = 0;

    public MainPresenter(MainActivity mContext) {
        this.mContext = mContext;
        mainModel = new MainModel(mContext);
        data = new FishSurveyData(mContext);
        initArguments();
        CheckInitData();
    }

    private void initArguments() {
        handler = new fsElementHandler(mContext);
    }

    private void CheckInitData() {
        count = 0;
        int length = handler.getAllEntry().size();
        final ArrayList<fsElement> elements = data.CreateData();
        if (length <= 0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (count < 5) {
                        count++;
                        return;
                    } else {
                        if (position >= elements.size()) {
                            fCheckUserInfo();
                            this.cancel();
                            return;
                        }
                        final fsElement element = elements.get(position);
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (position == 0)
                                    mContext.initData(elements.size());
                                mContext.tvStatusText.setText(String.format("(%s/%s) %s", element.getID(), elements.size(), element.getName()));
                                mContext.prgHorizontial.setProgress(position);
                                try {
                                    handler.addEntry(element);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                position++;
                            }
                        });
                    }
                }
            }, 100, 100);


        } else
            fCheckUserInfo();
    }

    public void fCheckUserInfo() {
        count = 0;
        mContext.UserData();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (count < 30)
                {
                    count++;
                    return;
                }
                else {
                    this.cancel();
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!mainModel.user.get()) {
                                // If info of user not exists or not enough then start acivity for ask new
                                mContext.startActivity(new Intent(mContext, fsNewUserActivity.class));
                                mContext.finish();
                            } else {
                                mContext.startActivity(new Intent(mContext, fsCategorizeActivity.class));
                                mContext.finish();
                            }
                        }
                    });
                }
            }
        }, 100, 100);
    }
}
