package com.blogspot.tndev1403.fishSurvey.Model.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;
import com.blogspot.tndev1403.fishSurvey.TNLib;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class API {
    public final static String HOST_KEY = "HOST_KEY";

    public static class UpdateHost extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                URL url = new URL(ApplicationConfig.HostCheckAddress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                if (conn.getResponseCode() == 200) {
                    String Host = TNLib.InputStreamToString(is);
                    Log.w("HOST____", "doInBackground: " + Host);
                    if (!Host.equals(ApplicationConfig.Host))
                        ApplicationConfig.Host = Host;
                    return true;
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e("ERRORXXXX", e.getMessage());
            }
            return false;
        }
    }

    public static class Captain {
        public static final String ID = "id";
        public static final String FULL_NAME = "fullname";
        public static final String PHONE = "phone";
        public static final String VESSEL = "vessel";
    }

    public static class Record {
        public final static String CAPTAIN_ID = "captain_id";

        public static class trip {
            public final static String FROM_DATE = "from_date";
            public final static String TO_DATE = "to_date";
            public final static String DESCRIPTION = "description";
        }

        public final static String FISH_ID = "fish_id";
        public final static String LONG = "long";
        public final static String WEIGHT = "weight";
        public final static String LAT = "lat";
        public final static String LNG = "lng";
        public final static String CATCHED_AT = "catched_at";
    }

    public static class Image {
        public final static String RECORD_ID = "record_id";
        public final static String BASE64_CONTENT = "base64_content";
    }
}
