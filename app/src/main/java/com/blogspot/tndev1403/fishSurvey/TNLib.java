package com.blogspot.tndev1403.fishSurvey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class TNLib {
    public final static String TAG = "TNLib";
    public static class Location {
        public static Address getAddress(Context mContext, double lat, double lng) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                Address obj = addresses.get(0);
                return obj;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "getAddress: " +e.getMessage() );
            }
            return null;
        }
    }
    public static class Using {
        public static Bitmap BytesToBitmap(byte[] bytes) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        public static byte[] BitmapToBytes(Bitmap bm) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
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
        public static String DateToString(Date inp){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
            try {
                String result = simpleDateFormat.format(inp);
                return result;
            } catch (Exception e) {
                Log.e(TAG, "DateToString: " + e.getMessage() );
                return "";
            }
        }
        public static Date StringToDateTime(String inp) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
            try {
                Date myDate = simpleDateFormat.parse(inp);
                return myDate;
            } catch (Exception e) {
                return null;
            }
        }
        public static Date StringToDate(String inp) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date myDate = simpleDateFormat.parse(inp);
                return myDate;
            } catch (Exception e) {
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
