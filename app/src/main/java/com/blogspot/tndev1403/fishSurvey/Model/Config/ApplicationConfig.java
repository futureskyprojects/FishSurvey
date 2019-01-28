package com.blogspot.tndev1403.fishSurvey.Model.Config;

public class ApplicationConfig {
    public final static String TAG = "ApplicationConfig";
    public final static String Host = "";
    public final static String Key = "";
    public final static String Element = "";
    /* For categorize API */
    public static class CategorizeAPI {
        public final static String URL = "https://raw.githubusercontent.com/futureskyprojects/CodeExperience/master/fsCategorize.json";
        public final static String RootKey = "categorize";
        public final static String ID = "ID";
        public final static String Name = "Name";
        public final static String FeatureImage = "FeatureImage";
    }
    /* For Database */
    public final static String DATABASE_NAME = "FishSurvey";
    public final static int DATABASE_VERSION = 1;
}
