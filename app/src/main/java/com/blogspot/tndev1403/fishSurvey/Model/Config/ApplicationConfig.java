package com.blogspot.tndev1403.fishSurvey.Model.Config;

public class ApplicationConfig {
    public final static String TAG = "ApplicationConfig";
    public final static String Host = "";
    public final static String Key = "";
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
    }
    /* For permission code */
    public static class PERMISSION {
        public final static int CAMERA = 10001;
        public final static int LOCATION_GPS = 10002;
    }

}
