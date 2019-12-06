package com.blogspot.tndev1403.fishSurvey.Model.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class SyncDataService extends Service {
    public final static String TAG = "SYNC_DATA_SERVICES";
    NotificationManager notificationManager;
    Notification notification;
    Notification.Builder notificationBuilder;
    PendingIntent pendingIntent;
    fsCatchedHandler catchedHandler;
    ArrayList<fsCatched> cathceds;
    Intent mIntent;

    SharedPreferences preferences;
    boolean IS_FINISHED_CAPTAIN_SYNC = false;
    boolean isUpAndSyncCaptain = false;
    boolean isSyncRecording = false;
    Timer mServiceTimer;
    int TYPE = -1;
    boolean IS_FINISH_HOST = false;

    Handler viewHandler = new Handler();
    fsUser user;

    public SyncDataService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mIntent = intent;
        preferences = getSharedPreferences("TRIP", Context.MODE_PRIVATE);
        Init();
        catchedHandler = new fsCatchedHandler(this);
        cathceds = new ArrayList<>();
        showNotification();
        TimerCheckInternet();
        return START_STICKY;
    }

    private void Init() {
        try {
            if (new API.UpdateHost().execute(ApplicationConfig.Host).get()) {
                Log.w(TAG, "Init: Success upadate host " + ApplicationConfig.Host);
                IS_FINISH_HOST = true;
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.can_not_update_host_address, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notification.flags = STOP_FOREGROUND_REMOVE;
        }
        notification.contentIntent = null;
        notificationManager.cancelAll();
        notificationManager.notify(ApplicationConfig.CODE.NOTIFICATION_REQUEST_CODE, notification);
        notificationManager.cancelAll();
        stopForeground(true);
        this.stopSelf();
    }

    void SyncRecord(final fsCatched catched) {
        final JSONObject JSONSend = new JSONObject();
        try {
            JSONSend.put(API.Record.CAPTAIN_ID, Integer.parseInt(user.getUserID()));
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
            JSONSend.put(API.Record.LNG, catched.getLongitude());
            JSONSend.put(API.Record.CATCHED_AT, catched.getCatchedTime());
            Log.w(TAG, "SyncRecord: " + JSONSend.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //////
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //region Send Infor
                    URL url = new URL(ApplicationConfig.GetTrueURL(ApplicationConfig.Record));
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
                    int RecordID = -1;
                    String Respone = TNLib.InputStreamToString(is);
                    if (conn.getResponseCode() == 200) {
                        JSONObject object = new JSONObject(Respone);
                        object = object.getJSONObject("data");
                        RecordID = object.getInt(API.Image.RECORD_ID);
                    }
                    conn.disconnect();
                    //endregion

                    //region Upload Images
                    if (!catched.getImagePath().trim().isEmpty()) {
                        int ImageSent = 0;
                        String[] FileNames = catched.getImagePath().trim().split(" ");
                        for (String x : FileNames) {
                            String FileName = ApplicationConfig.FOLDER.APP_DIR + File.separator + x;
                            // --- Declare JSON
                            JSONObject ImageJSONObject = new JSONObject();
                            ImageJSONObject.put(API.Image.RECORD_ID, RecordID);
                            ImageJSONObject.put(API.Image.BASE64_CONTENT, TNLib.Using.Base64FromImageFile(FileName));
                            // --- Send it
                            URL _url = new URL(ApplicationConfig.GetTrueURL(ApplicationConfig.Image));
                            HttpURLConnection _conn = (HttpURLConnection) _url.openConnection();
                            _conn.setRequestMethod("POST");
                            _conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                            _conn.setRequestProperty("Accept", "application/json");
                            _conn.setDoOutput(true);
                            _conn.setDoInput(true);
                            DataOutputStream _os = new DataOutputStream(_conn.getOutputStream());
                            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                            _os.writeBytes(ImageJSONObject.toString());

                            _os.flush();
                            _os.close();
                            InputStream _is = _conn.getInputStream();
                            Log.w(TAG, "[" + String.valueOf(conn.getResponseCode()) + "]Upload Image Respone: " + TNLib.InputStreamToString(_is));
                            if (_conn.getResponseCode() == 200) {
                                ImageSent++;
                                if (TNLib.Using.DeleteFile(FileName)) {
                                    catched.setImagePath(catched.getImagePath().replace(x, ""));
                                    catchedHandler.updateEntry(catched);
                                }
                            }
                            _conn.disconnect();
                        }
                        if (ImageSent >= FileNames.length) {
                            CleanFinishRecord(catched);
                        }
                    } else {
                        CleanFinishRecord(catched);
                    }
                    //endregion

                } catch (Exception e) {
                    Log.e("API", "Send: " + e.getMessage());
                }
            }
        }).start();
    }

    void CleanFinishRecord(fsCatched catched) {
        catchedHandler.deleteEntry(catched.getID());
        cathceds.remove(catched);
        if (cathceds.isEmpty()) {
            mServiceTimer.cancel();
            stopSelf();
            viewHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toasty.success(SyncDataService.this, R.string.sync_finish_success, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    //region Sync Records
    void SyncRecords() {
        GetAndCheckData();
        if (cathceds.size() <= 0) {
            stopSelf();
            return;
        }
        viewHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SyncDataService.this, "synchronizing...", Toast.LENGTH_SHORT).show();
            }
        });
        SyncNotificaion();
        for (int i = 0; i < cathceds.size(); i++) {
            final fsCatched catched = cathceds.get(i);
            SyncRecord(catched);
        }

    }
    //endregion


    void UpAndSyncCaptain() {
        if (isUpAndSyncCaptain)
            return;
        else
            isUpAndSyncCaptain = true;
        SyncNotificaion();
        user = new fsUser(this);
        final JSONObject JSONSend = new JSONObject();
        try {
            if (!user.getUserID().isEmpty() && Integer.parseInt(user.getUserID()) != -1) {
                Log.w(TAG, "UpAndSyncCaptain: " + "OK have ID is " + user.getUserID());
                JSONSend.put(API.Captain.ID, Integer.parseInt(user.getUserID()));
            }
            JSONSend.put(API.Captain.FULL_NAME, URLEncoder.encode(user.getUserName(), "UTF-8"));
            JSONSend.put(API.Captain.PHONE, user.getPhoneNumber());
            JSONSend.put(API.Captain.VESSEL, URLEncoder.encode(user.getBoatCode(), "UTF-8"));
            Log.w(TAG, "JSON: " + JSONSend.toString());
        } catch (Exception e) {
            Log.e(TAG, "JSON PRASE: " + e.getMessage());
            e.printStackTrace();
        }
        //////
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ApplicationConfig.GetTrueURL(ApplicationConfig.Captiain));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json"); //;charset=UTF-8
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(JSONSend.toString());
//                    os.writeUTF(JSONSend.toString());
                    Log.w(TAG, "PREPARE: " + JSONSend.toString());
                    os.flush();
                    os.close();

                    InputStream is = conn.getInputStream();
                    String response = TNLib.InputStreamToString(is);
                    int response_code = conn.getResponseCode();
                    if (response_code == 200) {
                        JSONObject obj = new JSONObject(response);
                        String ResponeID = obj.getJSONObject("data").getString(API.Captain.ID);
                        user.setUserID(ResponeID);
                        user.commit();
                    }
                    Log.w(TAG, "run: " + response);
                    Log.w("STATUS", "[" + conn.getURL() + "]" + response_code + "\n" + conn.getContent().toString());
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
        String ID = preferences.getString(fsHomePresenter.ID_KEY, "");
        cathceds = catchedHandler.getAllEntryDifferentWithCurrentTripID(ID);
        if (cathceds.size() <= 0) {
            stopSelf();
            return;
        }
        for (int i = 0; i < cathceds.size(); i++) {
            if (cathceds.get(i).getFinished_time().trim().isEmpty())
                cathceds.remove(i);
        }
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
        notificationBuilder.setContentTitle(getResources().getString(R.string.synchronizing).toUpperCase());
        notificationBuilder.setProgress(100, 30, true);
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
        notificationBuilder.setContentTitle(getResources().getString(R.string.waiting_internet).toUpperCase());
        notificationBuilder.setContentText(getResources().getString(R.string.need_network_to_finish));
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
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new Notification.Builder(this);
        /* Apply config */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int importantce = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                importantce = NotificationManager.IMPORTANCE_HIGH;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = notificationManager.getNotificationChannel("1998");
                if (mChannel == null) {
                    mChannel = new NotificationChannel("1998", "Test", importantce);
                    notificationManager.createNotificationChannel(mChannel);
                }
            }
            notification = notificationBuilder.build();
        }
        notification.icon = R.drawable.ic_cloud_sync; // icon for services
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.contentIntent = pendingIntent;
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
                    Reset();
                } else {
                    // Have internet
                    if (!IS_FINISH_HOST)
                        Init();
                    else if (!IS_FINISHED_CAPTAIN_SYNC)
                        UpAndSyncCaptain();
                    else
                        RunSync();
                }
            }
        }, 200, 300);
    }

    //endregion
    void RunSync() {
        if (user.getUserID() == "-1") {
            stopSelf();
            return;
        }
        if (!isSyncRecording) {
            isSyncRecording = true;
            SyncRecords();
        }
    }
    void Reset() {
        isSyncRecording = false;
        IS_FINISH_HOST = false;
        IS_FINISHED_CAPTAIN_SYNC = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Don't use it
        return null;
    }
}
