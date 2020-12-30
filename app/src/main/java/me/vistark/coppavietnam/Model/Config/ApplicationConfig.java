package me.vistark.coppavietnam.Model.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import me.vistark.coppavietnam.TNLib;

import java.io.File;

public class ApplicationConfig {
    public final static String APP_NAME = "COPPA VietNam";
    public final static String TAG = "ApplicationConfig";
    public static String Host = "http://coppa.vn";
    public final static String Key = "api";
    public final static String Captiain = "captain";
    public final static String Record = "record";
    public final static String Image = "image";
    public final static String HostCheckAddress = "https://raw.githubusercontent.com/futureskyprojects/CodeExperience/master/Host.coppa";
    public static String GetTrueURL(String kind) {
        return ("http://" + (Host + "/" + Key + "/" + kind).replace("http://","")
                .replace("https://", "")
                .replace("//","/")).replace("\n","").trim();
    }
    /* For categorize API */
    public static class CategorizeAPI {
        public final static String URL = "https://raw.githubusercontent.com/futureskyprojects/CodeExperience/master/fsCategorize.json";
        public final static String RootKey = "categorize";
        public final static String ID = "ID";
        public final static String Name = "Name";
        public final static String FeatureImage = "FeatureImage";
    }
    /* For element API */
    public static class ElementAPI extends CategorizeAPI {
        public final static String URL = "https://raw.githubusercontent.com/futureskyprojects/CodeExperience/master/fsElement.json";
        public final static String RootKey = "elements";
        public final static String fsCategorizeID = "categorize_id";
    }
    /* For Database */
    public final static String DATABASE_NAME = "FishSurvey";
    public final static int DATABASE_VERSION = 1;
    /* For RESULT REQUEST CODE */
    public static class CODE {
        public final static int GALLERY_SELECT_REQUEST_CODE = 1403;
        public final static int IMAGE_CAPTURE_REQUEST_CODE = 1998;
        public final static int NOTIFICATION_REQUEST_CODE = 1999;
    }
    /* For permission code */
    public static class PERMISSION {
        public final static int ALL_PERMISSION = 14398;
        public final static int CAMERA = 10001;
        public final static int LOCATION_GPS = 10002;
        public final static int WRITE_EXTERNAL_STORAGE = 10003;
        public final static int READ_EXTERNAL_STORAGE = 10004;
    }
    /* App folder */
    public static class FOLDER {
        public final static String APP_EXTENSION = "cop";
        public final static String APP_DIR = Environment.getExternalStorageDirectory() +
                File.separator + "." + APP_NAME;
        public static boolean CheckAndCreate() {
            File folder = new File(APP_DIR);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            return success;
        }
    }
    /* App save language */
    public static class LANGUAGE {
        public final static String CODE = "LANGUAGE";
        public static String GetLanguageCode(Context mContext) {
            SharedPreferences preferences = mContext.getSharedPreferences(CODE, Context.MODE_PRIVATE);
            return preferences.getString(CODE, "en");
        }
        public static void InitLanguage(Context mContext) {
            SharedPreferences preferences = mContext.getSharedPreferences(CODE, Context.MODE_PRIVATE);
            String code = preferences.getString(CODE, "en");
            TNLib.Using.ChangeLanguage(mContext, code);
        }
        public static void UpdateLanguage(Context mContext, String code) {
            SharedPreferences preferences = mContext.getSharedPreferences(CODE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CODE, code);
            editor.commit();
            TNLib.Using.ChangeLanguage(mContext, code);
        }
    }
}
