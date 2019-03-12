package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.blogspot.tndev1403.fishSurvey.MainActivity;
import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Config.FishSurveyData;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.Model.MainModel;
import com.blogspot.tndev1403.fishSurvey.Model.fsElementHandler;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.View.fsHome;
import com.blogspot.tndev1403.fishSurvey.View.fsNewUserActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainPresenter {
    MainActivity mContext;
    MainModel mainModel;
    fsElementHandler handler;
    FishSurveyData data;

    public MainPresenter(MainActivity mContext) {
        this.mContext = mContext;
        mainModel = new MainModel(mContext);
        data = new FishSurveyData(mContext);
        initArguments();
        CheckPermission();
    }

    public void CallCheckInitData() {
        new CheckInitData().execute();
    }
    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mContext.initData(100);
                    mContext.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, ApplicationConfig.PERMISSION.ALL_PERMISSION);
                }
            } else
                CallCheckInitData();
        } else CallCheckInitData();
    }

    private void initArguments() {
        handler = new fsElementHandler(mContext);
    }

    public class CheckInitData extends AsyncTask<Void, Void, Boolean> {
        Handler handlerView = new Handler();
        ArrayList<fsElement> elements;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContext.prgHorizontial.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            int length = handler.getAllEntry().size();
            if (length < 50)
                handler.deleteAll();
            else
                return true;
            elements = data.CreateData();
            // ---- //
            handlerView.post(new Runnable() {
                @Override
                public void run() {
                    mContext.initData(elements.size());
                }
            });
            // ---- //
            if (length <= 0) {
                for (int i = 0; i < elements.size(); i++) {
                    final fsElement element = elements.get(i);
                    try {
                        handler.addEntry(element);
                        final int finalI = i;
                        handlerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mContext.tvStatusText.setText(String.format("(%s/%s) %s", element.getID(), elements.size(), element.getName()));
                                mContext.prgHorizontial.setProgress(finalI + 1);
                            }
                        });
                        Thread.sleep(2);
                    } catch (Exception e) {
                        Log.w(MainPresenter.class.getSimpleName(), e.getMessage());
                        return false;
                    }
                }
                return true;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean)
                new CheckUserInfo().execute();
            else {
                final SweetAlertDialog alertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.error).toUpperCase())
                        .setContentText(mContext.getResources().getString(R.string.extract_data_fail))
                        .setConfirmButton(mContext.getResources().getString(R.string.close), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                handlerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mContext.finish();
                                    }
                                });
                            }
                        });
                alertDialog.show();
                handler.deleteAll();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handlerView.post(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.cancel();
                                mContext.finish();
                            }
                        });
                    }
                });
            }
        }
    }

    class CheckUserInfo extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContext.UserData();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mainModel.user.get();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean)
            {
                mContext.startActivity(new Intent(mContext, fsHome.class));
                mContext.finish();
            } else {
                mContext.startActivity(new Intent(mContext, fsNewUserActivity.class));
                mContext.finish();
            }
        }
    }
}
