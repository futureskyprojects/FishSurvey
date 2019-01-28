package com.blogspot.tndev1403.fishSurvey.Model.Entity;

import org.json.JSONObject;

import java.util.Date;

public class fsCatched {
    public static class Key {
        public static String ID = "ID";
        public static String ElementID = "ElementID";
        public static String CreatedDate = "CreatedDate";
        public static String Length = "Length";
        public static String Weight = "Weight";
        public static String CatchedTime = "CatchedTime";
        public static String Latitude = "Latitude";
        public static String Longitude = "Logitude";
        public static String ImagePath = "ImagePath";
    }
    int ID;
    int ElementID;
    Date CreatedDate;
    float Length;
    float Weight;
    Date CatchedTime;
    String Latitude;
    String Longitude;
    String ImagePath;

    public fsCatched(int ID, int elementID, Date createdDate, float length, float weight, Date catchedTime, String latitude, String longitude, String imagePath) {
        this.ID = ID;
        ElementID = elementID;
        CreatedDate = createdDate;
        Length = length;
        Weight = weight;
        CatchedTime = catchedTime;
        Latitude = latitude;
        Longitude = longitude;
        ImagePath = imagePath;
    }
//    public String getJSON() {
//        JSONObject catched = new JSONObject();
//        catched.put()
//    }
}
