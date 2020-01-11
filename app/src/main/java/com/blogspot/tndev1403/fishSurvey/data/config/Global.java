package com.blogspot.tndev1403.fishSurvey.data.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;
import com.blogspot.tndev1403.fishSurvey.view.fishcatchinputscreen.FishCatchInputActivity;

import java.io.File;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Use for config all project. Save global variable for usage.
 */

public class Global {
    private final static String APP_NAME = "COPPA"; // Set name of application
    public static String Host = "http://coppa.vn"; // Default REST host
    private final static String Key = "api"; // Default API path key
    public final static String Captiain = "captain"; // Captain API key
    public final static String Record = "record"; // Record API key
    public final static String Image = "image"; // Image API key
    public final static String HostCheckAddress = // For check default REST host if change address
            "https://raw.githubusercontent.com/futureskyprojects/CodeExperience/master/Host.coppa";

    // Method for get, check and change REST host if it was change
    public static String getUpdateHostURL(String kind) {
        return ("http://" +
                (Host + "/" + Key + "/" + kind)
                        .replace("http://", "") // Delete http if it have, to add a new above
                        .replace("https://", "") // Delete https if it have, to add a new above
                        .replace("//", "/") // if it have '//', change it to '/'
        )
                .replace("\n", "") // Don't accept newline
                .trim(); // Remove space from start and and if have
    }

    /* Key of properties of Category API */
    public static class CategorizeAPI {
        public final static String ID = "ID";
        public final static String Name = "Name";
    }

    /* Statics properties for database */
    public final static String DATABASE_NAME = "FishSurvey";
    public final static int DATABASE_VERSION = 1;

    /* Requests code */
    public static class CODE {
        public final static int GALLERY_SELECT_REQUEST_CODE = 1403;
        public final static int IMAGE_CAPTURE_REQUEST_CODE = 1998;
        public final static int NOTIFICATION_REQUEST_CODE = 1999;
    }

    /* Permissions code */
    public static class PERMISSION {
        public final static int ALL_PERMISSION = 14398;
        public final static int CAMERA = 10001;
        public final static int LOCATION_GPS = 10002;
        public final static int WRITE_EXTERNAL_STORAGE = 10003;
        public final static int READ_EXTERNAL_STORAGE = 10004;
    }

    /* Class for check and init COPPA work folder */
    public static class CoppaFiles {
        public final static String APP_EXTENSION = "cop"; // Define image file of this app is *.cop
        public static String APP_DIR = ""; // Contain work directory path

        // Method for init work directory
        public static boolean checkAndCreateCoppaDirectory(FishCatchInputActivity mContext) {
            if (APP_DIR.isEmpty()) {
                // If APP_DIR is empty, init new
                APP_DIR = mContext.getCacheDir() +
                        File.separator + "." + APP_NAME;
            }

            // Code for create new directory if it not exists
            File folder = new File(APP_DIR);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            return success;
        }
    }

    /* App language save class */
    public static class CoppaLanguage {
        final static String CODE = "CoppaLanguage"; // SharedPreferences key for language save

        // Method for get saved language code
        public static String getLanguageCode(Context mContext) {
            SharedPreferences preferences = mContext.getSharedPreferences(CODE, Context.MODE_PRIVATE);
            return preferences.getString(CODE, "en");
        }

        // Method for init default/saved language for application when it start
        public static void initLanguage(Context mContext) {
            SharedPreferences preferences = mContext.getSharedPreferences(CODE, Context.MODE_PRIVATE);
            String code = preferences.getString(CODE, "en");

            // Change all string in this application to Default/Saved language
            ProcessingLibrary.Using.ChangeLanguage(mContext, code);
        }

        // Method for update language code when user change language
        public static void updateLanguage(Context mContext, String code) {
            SharedPreferences preferences = mContext.getSharedPreferences(CODE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CODE, code);
            editor.apply();

            // Change all string in this application to new language
            ProcessingLibrary.Using.ChangeLanguage(mContext, code);
        }
    }
}
