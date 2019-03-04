package com.blogspot.tndev1403.fishSurvey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        public static String StringListToSingalString(ArrayList<String> arr) {
            String res = "";
            for (int i = 0; i < arr.size();i++) {
                res += arr.get(i) + " ";
            }
            return res;
        }
        public static Bitmap BitmapFromFilePath(String _Path) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(_Path, options);
            return bitmap;
        }
        public static boolean SaveImage(Bitmap bm, String fn, String destDir) {
            // Check and create dir if nessasary
            File dest = new File(destDir);
            if (!dest.exists()) {
                return false;
            }
            File desF = new File(dest + File.separator + fn);
            if (desF.exists()) {
                desF.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(desF);
                out.write(TNLib.Using.BitmapToBytes(bm));
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                Log.e(TAG, "SaveImage: " + e.getMessage());
                return false;
            }

        }
        private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

            File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

            if (!direct.exists()) {
                File wallpaperDirectory = new File("/sdcard/DirName/");
                wallpaperDirectory.mkdirs();
            }

            File file = new File(new File("/sdcard/DirName/"), fileName);
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss'Z'dd/MM/yyyy'T'");
            try {
                String result = simpleDateFormat.format(inp);
                return result;
            } catch (Exception e) {
                Log.e(TAG, "DateToString: " + e.getMessage() );
                return "";
            }
        }
        public static Date StringToDateTime(String inp) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss'Z'dd/MM/yyyy'T'");
            try {
                Date myDate = simpleDateFormat.parse(inp);
                return myDate;
            } catch (Exception e) {
                Log.e(TAG, "StringToDateTime: " + e.getMessage());
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

        public static Bitmap DrawableToBitmap(Context mContext, int id) {
            return ((BitmapDrawable) mContext.getResources().getDrawable(id)).getBitmap();
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
