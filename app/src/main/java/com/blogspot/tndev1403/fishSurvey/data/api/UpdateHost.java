package com.blogspot.tndev1403.fishSurvey.data.api;

import android.os.AsyncTask;

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: For sync REST host if it have any change
 */

public class UpdateHost extends AsyncTask<String, Integer, Boolean> {
    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL url = new URL(Global.HostCheckAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                String Host = ProcessingLibrary.InputStreamToString(is);
                if (!Host.equals(Global.Host)) {
                    // If REST host was changed, update it
                    Global.Host = Host;
                }
                return true;
            }
            conn.disconnect();
        } catch (Exception ignore) {
        }
        return false;
    }
}