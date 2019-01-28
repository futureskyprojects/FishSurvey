package com.blogspot.tndev1403.fishSurvey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class TNLib {
    public final static String TAG = "TNLib";
    public static class Using {
        public static String getContent(String _URL){
            String result = "";
            try {
                result = new getContentFromURL().execute(_URL).get();
            } catch (Exception e) {
                result = "Không thực hiện tải được " + e.getMessage();
            }
            Log.d(TAG, "getContent: " + result);
            return result;
        }
        public static Bitmap getBitmap(String _URL) {
            try {
                return new getBitmapFromURL().execute(_URL).get();
            } catch (Exception e) {
                Log.e(TAG, "getBitmap: " + e.getMessage());
                return null;
            }
        }
    }
    public static class getContentFromURL extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                return InputStringToString(conn.getInputStream());
            } catch (Exception e) {
                Log.e(TAG, "<getContentFromURL> " + e.getMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    public static class getBitmapFromURL extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... imageUrl) {
            try {
                URL url = new URL(imageUrl[0].replace("https","http"));
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }
    //region InputStream to String
    public static String InputStringToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "> " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "> " + e.getMessage());
            }
        }
        return sb.toString();
    }
    //endregion
}
