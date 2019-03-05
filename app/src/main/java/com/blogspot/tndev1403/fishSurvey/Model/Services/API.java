package com.blogspot.tndev1403.fishSurvey.Model.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsUser;

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
        public static class CreateNew extends AsyncTask<String, String, String> {
            public static void Send(final Context mContext) {
                fsUser user = new fsUser(mContext);
                final JSONObject JSONSend = new JSONObject();

                try {
                    JSONSend.put(FULL_NAME, user.getUserName());
                    JSONSend.put(PHONE, user.getPhoneNumber());
                    JSONSend.put(VESSEL, user.getBoatCode());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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

                            Log.w("STATUS", String.valueOf(conn.getResponseCode()));
                            Log.w("MSG", conn.getResponseMessage());
                            conn.disconnect();

                        } catch (Exception e) {
                            Log.e("API", "Send: " + e.getMessage() );
                        }
                    }
                }).start();
            }
            public CreateNew(){
                //set context variables if required
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                String FullName = params[0];
                String Phone = params[1];
                String Vessel = params[2];
                OutputStream out = null;
                try {
                    URL url = new URL(ApplicationConfig.Captiain);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    out = new BufferedOutputStream(urlConnection.getOutputStream());

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//                    writer.write(data);
                    writer.flush();
                    writer.close();
                    out.close();

                    urlConnection.connect();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return "";
            }
        }
    }
}
