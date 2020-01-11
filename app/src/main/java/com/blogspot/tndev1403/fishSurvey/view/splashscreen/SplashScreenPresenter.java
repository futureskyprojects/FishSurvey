package com.blogspot.tndev1403.fishSurvey.view.splashscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.View;

import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.data.config.DefaultFishData;
import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.data.db.FishHandler;
import com.blogspot.tndev1403.fishSurvey.model.Captain;
import com.blogspot.tndev1403.fishSurvey.model.Fish;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenActivity;
import com.blogspot.tndev1403.fishSurvey.view.newcaptainscreen.NewCaptainScreenActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

class SplashScreenPresenter {
    private SplashScreenActivity mContext;
    private Captain captain;
    private FishHandler fishHandler;
    private DefaultFishData defaultFishData;

    SplashScreenPresenter(SplashScreenActivity mContext) {
        this.mContext = mContext;
        this.captain = new Captain(mContext);
        this.defaultFishData = new DefaultFishData(mContext);
        this.fishHandler = new FishHandler(mContext);

        // Check permission
        CheckPermission();
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                mContext.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, Global.PERMISSION.ALL_PERMISSION);
            } else
                CallCheckInitData();
        } else CallCheckInitData();
    }

    void CallCheckInitData() {
        new CheckInitData().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class CheckInitData extends AsyncTask<Void, Void, Boolean> {
        Handler handlerView = new Handler();
        ArrayList<Fish> elements;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContext.prgHorizontial.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            int length = fishHandler.get().size();
            if (length < 50)
                fishHandler.deleteAll();
            else
                return true;
            elements = defaultFishData.generate();

            // Show progress bar
            handlerView.post(new Runnable() {
                @Override
                public void run() {
                    mContext.showInitView(elements.size());
                }
            });

            // Update progress
            if (length <= 0) {
                for (int i = 0; i < elements.size(); i++) {
                    final Fish element = elements.get(i);
                    try {
                        fishHandler.add(element);
                        final int finalI = i;
                        handlerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mContext.tvStatusText.setText(String.format("(%s/%s) %s", element.getId(), elements.size(), element.getName()));
                                mContext.prgHorizontial.setProgress(finalI + 1);
                            }
                        });
                        Thread.sleep(2);
                    } catch (Exception e) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                // check user info after init fish data
                new CheckUserInfo().execute();
            } else {
                // Announcement if have error
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

                // And delete all error data
                fishHandler.deleteAll();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ignore) {
                        }

                        // Close dialog and finish application
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

    // Check user info class
    @SuppressLint("StaticFieldLeak")
    class CheckUserInfo extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContext.hideInitView();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return captain.get();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                mContext.startActivity(new Intent(mContext, HomeScreenActivity.class));
                mContext.finish();
            } else {
                mContext.startActivity(new Intent(mContext, NewCaptainScreenActivity.class));
                mContext.finish();
            }
        }
    }
}
