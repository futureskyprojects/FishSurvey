package com.blogspot.tndev1403.fishSurvey.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Base64;

import androidx.core.graphics.BitmapCompat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProcessingLibrary {

    public static class Using {
        // Delete file by path method
        public static boolean DeleteFile(String Path) {
            File file = new File(Path);
            return file.delete();
        }

        // Method change language
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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(new Date(Long.parseLong(timestamp) * 1000));
        }

        public static boolean IsMyServiceRunning(Context mContext, Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            if (manager != null) {
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (serviceClass.getName().equals(service.service.getClassName())) {
                        return true;
                    }
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
            );
            return s;
        }

        public static String MyCalendarToReverseString(Calendar calendarX) {
            return calendarX.get(Calendar.YEAR) + "-" + (calendarX.get(Calendar.MONTH) + 1) +
                    "-" + calendarX.get(Calendar.DAY_OF_MONTH) + " " + calendarX.get(Calendar.HOUR_OF_DAY) + ":" +
                    calendarX.get(Calendar.MINUTE) + ":" + calendarX.get(Calendar.SECOND);
        }

        static String MyCalendarToString(Calendar calendarX) {
            return calendarX.get(Calendar.HOUR_OF_DAY) + ":" +
                    calendarX.get(Calendar.MINUTE) + " " + calendarX.get(Calendar.DAY_OF_MONTH) + "-" +
                    (calendarX.get(Calendar.MONTH) + 1) + "-" + calendarX.get(Calendar.YEAR);
        }

        public static String GetNowTimeString() {
            Calendar calendarX = Calendar.getInstance();
            return MyCalendarToString(calendarX);
        }

        public static String GetCurrentTimeStamp() {
            long tsLong = System.currentTimeMillis() / 1000;
            return Long.toString(tsLong);
        }

        public static String StringListToSingalString(ArrayList<String> arr) {
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                res.append(arr.get(i)).append(" ");
            }
            return res.toString();
        }

        public static String Base64FromImageFile(String _Path) {
            File file = new File(_Path);
            int size = (int) file.length();
            byte[] bytes = new byte[size];

            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
                return Base64.encodeToString(bytes, Base64.DEFAULT);
            } catch (Exception e) {
                Bitmap bm = BitmapFromFilePath(_Path);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                return Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
        }

        public static Bitmap BitmapFromFilePath(String _Path) {
            return BitmapFactory.decodeFile(_Path);
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
                out.write(ProcessingLibrary.Using.BitmapToBytes(bm));
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                return false;
            }

        }

        static Bitmap ResizeBitmap(Bitmap image, int maxSize) {
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
            int maxSize = 2048;
            if (bm.getWidth() > maxSize || bm.getHeight() > maxSize) {
                bm = ResizeBitmap(bm, maxSize);
            }
            int currentSize = BitmapCompat.getAllocationByteCount(bm);
            float requireSize = (float) (1024 * 1024);
            if (currentSize > requireSize) {
                int ration = (int) (currentSize * 100 / requireSize);
                if (ration > 100 && ration < 200 && quality - (ration - 100) > min) {
                    quality = quality - ration;
                } else {
                    quality = min;
                }
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            return stream.toByteArray();
        }

        public static Bitmap DrawableToBitmap(Context mContext, int id) {
            return ((BitmapDrawable) mContext.getResources().getDrawable(id)).getBitmap();
        }
    }

    //region InputStream to String
    public static String InputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception ignore) {
        }
        return sb.toString();
    }
    //endregion
}
