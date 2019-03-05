package com.blogspot.tndev1403.fishSurvey.Model.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsHomePresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.View.fsHome;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SyncDataService extends Service {
    public final static String TAG = "SYNC_DATA_SERVICES";
    NotificationManager notificationManager;
    Notification notification;
    Notification.Builder notificationBuilder;
    PendingIntent pendingIntent;
    fsCatchedHandler catchedHandler;
    ArrayList<fsCatched> cathceds;
    Intent mIntent;
    boolean IS_HAVE_INTERNET = false;
    public SyncDataService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mIntent = intent;
        catchedHandler = new fsCatchedHandler(this);
        cathceds = new ArrayList<>();
        showNotification();
        GetAndCheckData();
        TimerCheckInternet();
        return START_STICKY;
    }
    void UpAndSyncCaptain() {

    }

    private void GetAndCheckData() {
        cathceds = catchedHandler.getAllEntryDifferentWithCurrentTripID(fsHomePresenter.CURRENT_TRIP_ID);
        for (int i = 0; i < cathceds.size(); i++) {
            if (cathceds.get(i).getFinished_time().trim().isEmpty())
                cathceds.remove(i);
        }
        UpdateProgress(0);
    }
    void UpdateProgress(int Current) {
        notificationBuilder.setContentText(Current + "/" + cathceds.size());
        notificationBuilder.setProgress(cathceds.size(), 0, false);
    }


    //region Notification
    void SyncNotificaion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(this,
                    R.color.blue_btn_bg_pressed_color));
        }
        notificationBuilder.setContentTitle("ĐỒNG BỘ");
        notificationManager.cancelAll();
        notificationManager.notify(ApplicationConfig.CODE.NOTIFICATION_REQUEST_CODE, notification);
    }
    void WaitInternetNotificaion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(this,
                    R.color.blue_btn_bg_pressed_color));
        }
        notificationBuilder.setContentTitle("ĐỢI MẠNG");
        notificationBuilder.setContentText("Cần có mạng để hoàn tất");
        notificationBuilder.setProgress(100, 30, true);
        notificationManager.cancelAll();
        notificationManager.notify(ApplicationConfig.CODE.NOTIFICATION_REQUEST_CODE, notification);
    }
    void showNotification() {
        Intent notificationIntent = new Intent(this, fsHome.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, ApplicationConfig.CODE.NOTIFICATION_REQUEST_CODE,
                notificationIntent, 0);
        /* Create notification builder */
        notificationBuilder = new Notification.Builder(this);
        /* Apply config */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = notificationBuilder.build();
        }
        notification.icon = R.drawable.ic_cloud_sync; // icon for services
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.contentIntent = pendingIntent;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    void TimerCheckInternet() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // if not connect to the network, set warning
                if (!isInternetAvailable()) {
                    if (IS_HAVE_INTERNET)
                        IS_HAVE_INTERNET = false;
                    else
                        return;
                    // If havn't internet
                    WaitInternetNotificaion();
                } else {
                    if (!IS_HAVE_INTERNET)
                        IS_HAVE_INTERNET = true;
                    else
                        return;
                    // Have internet
                    SyncNotificaion();
                }
            }
        }, 200, 300);
    }
    //endregion

    @Override
    public IBinder onBind(Intent intent) {
        // Don't use it
        return null;
    }
}
