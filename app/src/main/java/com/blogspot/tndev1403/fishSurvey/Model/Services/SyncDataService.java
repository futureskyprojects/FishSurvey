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
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsHomePresenter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.TNLib;
import com.blogspot.tndev1403.fishSurvey.View.fsHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
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
    boolean IS_FINISHED_CAPTAIN_SYNC = false;
    int FINISHED_SYNC_RECORDS = 0;
    boolean isUpAndSyncCaptain = false;
    boolean isSyncRecording = false;
    Timer mServiceTimer;
    int CAPTAIN_ID = -1;
    int TYPE = -1;

    public SyncDataService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mIntent = intent;
        catchedHandler = new fsCatchedHandler(this);
        cathceds = new ArrayList<>();
        showNotification();
//        Test();
        TimerCheckInternet();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notification.flags = STOP_FOREGROUND_REMOVE;
        notification.contentIntent = null;
        notificationManager.cancelAll();
        notificationManager.notify(ApplicationConfig.CODE.NOTIFICATION_REQUEST_CODE, notification);
        notificationManager.cancelAll();
        stopForeground(true);
        this.stopSelf();
    }

    void SyncRecord(final fsCatched catched) {
        SyncNotificaion();
        final JSONObject JSONSend = new JSONObject();
        try {
            JSONSend.put(API.Record.CAPTAIN_ID, CAPTAIN_ID);
            // --- TRIP --- //
            JSONObject trip = new JSONObject();
            trip.put(API.Record.trip.FROM_DATE, TNLib.Using.DateTimeStringReverseFromTimeStamp(catched.getTrip_id()));
            trip.put(API.Record.trip.TO_DATE, catched.getFinished_time());
            trip.put(API.Record.trip.DESCRIPTION, "<Empty>");
            JSONSend.put(API.Record.trip.class.getSimpleName().toLowerCase(), trip);
            //-------------//
            JSONSend.put(API.Record.FISH_ID, catched.getElementID());
            JSONSend.put(API.Record.LONG, catched.getLength() + "");
            JSONSend.put(API.Record.WEIGHT, catched.getWeight() + "");
            JSONSend.put(API.Record.LAT, catched.getLatitude());
            JSONSend.put(API.Record.LNG, catched.getLatitude());
            JSONSend.put(API.Record.images.class.getSimpleName().toLowerCase(), GetJsonOfImages(catched));
            JSONSend.put(API.Record.CATCHED_AT, catched.getCatchedTime());
            Log.w(TAG, "SyncRecord: " + JSONSend.toString());
        } catch (JSONException e) {
            Log.e(TAG, "SyncRecords: " + e.getMessage());
            e.printStackTrace();
        }
        //////
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ApplicationConfig.Record);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(JSONSend.toString());

                    os.flush();
                    os.close();
                    InputStream is = conn.getInputStream();
                    Log.w(TAG, "run: " + TNLib.InputStreamToString(is));
                    Log.w("STATUS", String.valueOf(conn.getResponseCode()));
                    if (conn.getResponseCode() == 200) {
                        Log.d(TAG, "SyncRecords: Have JSON is " + JSONSend.toString());
                        catchedHandler.deleteEntry(catched.getID());
                        FINISHED_SYNC_RECORDS++;
                        Log.w(TAG, "run: " + TNLib.InputStreamToString(is));
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    Log.e("API", "Send: " + e.getMessage());
                }
                stopSelf();
            }
        }).start();
    }
    //region Sync Records
    void SyncRecords() {
        GetAndCheckData();
        if (cathceds.size() <= 0)
        {
            Log.w(TAG, "SyncRecords: Empty!" );
            stopSelf();
        }
        FINISHED_SYNC_RECORDS = 0;
        SyncNotificaion();
//        UpdateProgress(0);
        for (int i = 0; i < cathceds.size(); i++) {
            final fsCatched catched = cathceds.get(i);
            SyncRecord(catched);
        }
    }

    private Object GetJsonOfImages(fsCatched catched) {
        if (catched.getImagePath().trim().isEmpty())
            return new JSONObject();
        JSONArray imageArray = new JSONArray();
        String[] FileNames = catched.getImagePath().trim().split(" ");
        for (String x : FileNames) {
            try {
                JSONObject image = new JSONObject();
                image.put(API.Record.images.BASE_64_ENCODED, TNLib.Using.Base64FromImageFile(ApplicationConfig.FOLDER.APP_DIR + File.separator + x));
                imageArray.put(image);
            } catch (JSONException e) {
                Log.e(TAG, "GetJsonOfImages: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return (Object)imageArray;
    }
    //endregion


    void UpAndSyncCaptain() {
        if (isUpAndSyncCaptain)
            return;
        else
            isUpAndSyncCaptain = true;
        SyncCaptainInfoNotificaion();
        final fsUser user = new fsUser(this);
        final JSONObject JSONSend = new JSONObject();
        try {
            if (!user.getUserID().isEmpty()) {
                Log.w(TAG, "UpAndSyncCaptain: " + "OK have ID is " + user.getUserID());
                JSONSend.put(API.Captain.ID, user.getUserID());
            }
            JSONSend.put(API.Captain.FULL_NAME, user.getUserName());
            JSONSend.put(API.Captain.PHONE, user.getPhoneNumber());
            JSONSend.put(API.Captain.VESSEL, user.getBoatCode());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //////
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ApplicationConfig.Captiain);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(JSONSend.toString());

                    os.flush();
                    os.close();
                    InputStream is = conn.getInputStream();
                    if (conn.getResponseCode() == 200) {
                        JSONObject obj = new JSONObject(TNLib.InputStreamToString(is));
                        String ResponeID = obj.getJSONObject("data").getString(API.Captain.ID);
                        user.setUserID(ResponeID);
                        user.commit();
                        CAPTAIN_ID = Integer.parseInt(ResponeID);
                    }
                    Log.w(TAG, "run: " + TNLib.InputStreamToString(is));
                    Log.w("STATUS", String.valueOf(conn.getResponseCode()));
                    conn.disconnect();
                } catch (Exception e) {
                    Log.e("API", "Send: " + e.getMessage());
                }
                IS_FINISHED_CAPTAIN_SYNC = true;
                isUpAndSyncCaptain = false;
                RunSync();
            }
        }).start();
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
        if (TYPE != 1)
            TYPE = 1;
        else
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(this,
                    R.color.blue_btn_bg_pressed_color));
        }
        notificationBuilder.setContentTitle("ĐỒNG BỘ");
        notificationManager.cancelAll();
        notificationManager.notify(ApplicationConfig.CODE.NOTIFICATION_REQUEST_CODE, notification);
    }

    void SyncCaptainInfoNotificaion() {
        if (TYPE != 2)
            TYPE = 2;
        else
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(this,
                    R.color.blue_btn_bg_pressed_color));
        }
        notificationBuilder.setContentText("");
        notificationBuilder.setProgress(100, 30, true);
        notificationBuilder.setContentTitle("CẬP NHẬT NGƯỜI DÙNG");
        notificationManager.cancelAll();
        notificationManager.notify(ApplicationConfig.CODE.NOTIFICATION_REQUEST_CODE, notification);
    }

    void WaitInternetNotificaion() {
        if (TYPE != 3)
            TYPE = 3;
        else
            return;
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
        TYPE = 0;
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
        mServiceTimer = new Timer();
        mServiceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // if not connect to the network, set warning
                if (!isInternetAvailable()) {
                    // If havn't internet
                    WaitInternetNotificaion();
                } else {
                    // Have internet
                    if (!IS_FINISHED_CAPTAIN_SYNC)
                        UpAndSyncCaptain();
                    else if (FINISHED_SYNC_RECORDS < cathceds.size())
                        RunSync();
                    else {
                        mServiceTimer.cancel();
                        stopSelf();
                    }
                }
            }
        }, 200, 300);
    }
    //endregion
    void RunSync() {
        if (!isSyncRecording) {
            isSyncRecording = true;
            SyncRecords();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // Don't use it
        return null;
    }
}
