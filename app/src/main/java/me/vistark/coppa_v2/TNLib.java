package me.vistark.coppa_v2;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.core.graphics.BitmapCompat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                Log.e(TAG, "getAddress: " + e.getMessage());
            }
            return null;
        }
    }

    public static class Using {
        public static boolean DeleteFile(String Path) {
            File file = new File(Path);
            return file.delete();
        }

        public static String[] StringArrayListToStringArray(ArrayList<String> arr) {
            String[] strarr = new String[arr.size()];
            for (int i = 0; i < arr.size(); i++)
                strarr[i] = arr.get(i);
            return strarr;
        }

        public static void ChangeLanguage(Context mContext, String code) {
            Locale locale = new Locale(code);
            Resources resources = mContext.getResources();
            Configuration configuration = resources.getConfiguration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            }
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }

        public static String DateTimeStringReverseFromTimeStamp(String timestamp) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(new Date(Long.parseLong(timestamp) * 1000));
            return dateString;
        }

        public static String DateTimeStringFromTimeStamp(String timestamp) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateString = formatter.format(new Date(Long.parseLong(timestamp) * 1000));
            return dateString;
        }

        public static boolean IsMyServiceRunning(Context mContext, Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

        public static String GetReverseCurrentDateString() {
            Calendar calendarX = Calendar.getInstance();
            @SuppressLint("DefaultLocale") String s = String.format("%04d-%02d-%02d",// %02d:%02d:%02d",
                    calendarX.get(Calendar.YEAR),
                    calendarX.get(Calendar.MONTH) + 1,
                    calendarX.get(Calendar.DAY_OF_MONTH)
//                    ,
//                    calendarX.get(Calendar.HOUR_OF_DAY),
//                    calendarX.get(Calendar.MINUTE),
//                    calendarX.get(Calendar.SECOND)
            );
            return s;
        }

        public static String MyCalendarToReverseString(Calendar calendarX) {
            String str = calendarX.get(Calendar.YEAR) + "-" + (calendarX.get(Calendar.MONTH) + 1) +
                    "-" + calendarX.get(Calendar.DAY_OF_MONTH) + " " + calendarX.get(Calendar.HOUR_OF_DAY) + ":" +
                    calendarX.get(Calendar.MINUTE) + ":" + calendarX.get(Calendar.SECOND);
            return str;
        }

        public static String MyCalendarToString(Calendar calendarX) {
            String String = calendarX.get(Calendar.HOUR_OF_DAY) + ":" +
                    calendarX.get(Calendar.MINUTE) + " " + calendarX.get(Calendar.DAY_OF_MONTH) + "-" +
                    (calendarX.get(Calendar.MONTH) + 1) + "-" + calendarX.get(Calendar.YEAR);
            return String;
        }

        public static String GetNowTimeString() {
            Calendar calendarX = Calendar.getInstance();
            return MyCalendarToString(calendarX);
        }

        public static String GetCurrentTimeStamp() {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            return ts;
        }

        public static String StringListToSingalString(ArrayList<String> arr) {
            String res = "";
            for (int i = 0; i < arr.size(); i++) {
                res += arr.get(i) + " ";
            }
            Log.d(TAG, "StringListToSingalString: >" + res);
            return res;
        }

        public static String Base64FromImageFile(String _Path) {
            File file = new File(_Path);
            int size = (int) file.length();
            byte[] bytes = new byte[size];

            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
                String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
                return encoded;
            } catch (Exception e) {
                Bitmap bm = BitmapFromFilePath(_Path);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                return encoded;
            }
        }

        public static Bitmap BitmapFromFilePath(String _Path) {
            Bitmap bitmap = BitmapFactory.decodeFile(_Path);
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

        public static Bitmap BytesToBitmap(byte[] bytes) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        public static Bitmap ResizeBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }

            return Bitmap.createScaledBitmap(image, width, height, true);
        }

        // Phương thức nén để giảm chất lượng ảnh
        public static byte[] BitmapToBytes(Bitmap bm) {
            int quality = 100;
            int min = 20;
            Log.d(TAG, "BitmapToBytes: Kích cỡ: " + bm.getWidth() + "x" + bm.getHeight());
            int maxSize = 2048;
            if (bm.getWidth() > maxSize || bm.getHeight() > maxSize) {
                bm = ResizeBitmap(bm, maxSize);
            }
            int currentSize = BitmapCompat.getAllocationByteCount(bm);
            Log.d(TAG, "BitmapToBytes: Size: " + currentSize);
            float requireSize = (float) (1024 * 1024);
            if (currentSize > requireSize) {
                int ration = (int) (currentSize * 100 / requireSize);
                if (ration > 100 && ration < 200 && quality - (ration - 100) > min) {
                    quality = quality - ration;
                } else {
                    quality = min;
                }
            }
            Log.d(TAG, "BitmapToBytes: Chất lượng mới " + quality);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            Log.d(TAG, "BitmapToBytes: new Size: " + stream.size());
            return stream.toByteArray();
        }

        public static String getContent(String _URL) {
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

        public static String DateToString(Date inp) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss'Z'dd/MM/yyyy'T'");
            try {
                String result = simpleDateFormat.format(inp);
                return result;
            } catch (Exception e) {
                Log.e(TAG, "DateToString: " + e.getMessage());
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
                return InputStreamToString(conn.getInputStream());
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
                URL url = new URL(imageUrl[0].replace("https", "http"));
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
    public static String InputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "> " + e.getMessage());
        }
        return sb.toString();
    }
    //endregion
}
