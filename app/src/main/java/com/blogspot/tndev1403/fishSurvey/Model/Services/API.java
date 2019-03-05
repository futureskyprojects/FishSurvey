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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class API {
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
        public static class images {
            public final static String BASE_64_ENCODED = "Base64Encoded";
        }
        public final static String CATCHED_AT = "catched_at";
    }
}
