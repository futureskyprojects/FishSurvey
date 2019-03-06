package com.blogspot.tndev1403.fishSurvey.Model.Services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.blogspot.tndev1403.fishSurvey.Model.fsCatchedHandler;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsHomePresenter;
import com.blogspot.tndev1403.fishSurvey.TNLib;

import java.util.Timer;
import java.util.TimerTask;

public class SleepServices extends Service {
    fsCatchedHandler catchedHandler;
    SharedPreferences preferences;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return START_STICKY;
    }
    class RunCheckRealTime extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (Check() && !CheckRunningServices(SyncDataService.class))
                startService(new Intent(getApplicationContext(), SyncDataService.class));
            return null;
        }
    }
    boolean CheckRunningServices(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    boolean Check() {
        String ID = preferences.getString(fsHomePresenter.ID_KEY, "");
        int count = catchedHandler.CountNotSyncRecords(ID);
        if (count > 0)
            return true;
        return false;
    }

    private void init() {
        catchedHandler = new fsCatchedHandler(this);
        Log.w("SLEEP_SERVICE", "HERE!");
        preferences = getSharedPreferences("TRIP", Context.MODE_PRIVATE);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Check() && !CheckRunningServices(SyncDataService.class))
                    startService(new Intent(getApplicationContext(), SyncDataService.class));
                Log.w("SLEEP_SERVICES", "running...");
            }
        }, 100, 300);
    }
    public SleepServices() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
