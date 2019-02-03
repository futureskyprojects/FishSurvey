package com.blogspot.tndev1403.fishSurvey.Model.Entity;

import com.blogspot.tndev1403.fishSurvey.TNLib;

import org.json.JSONObject;

import java.util.Date;

public class fsCatched {
    int ID;
    int ElementID;
    String CreatedDate;
    float Length;
    float Weight;
    String CatchedTime;
    String Latitude;
    String Longitude;
    String ImagePath;

    public fsCatched(int ID, int elementID, String createdDate, String length, String weight, String catchedTime, String latitude, String longitude, String imagePath) {
        this.ID = ID;
        ElementID = elementID;
        CreatedDate = createdDate;
        Length = Float.parseFloat(length);
        Weight = Float.parseFloat(weight);
        CatchedTime = catchedTime;
        Latitude = latitude;
        Longitude = longitude;
        ImagePath = imagePath;
    }
//    public String getJSON() {
//        JSONObject catched = new JSONObject();
//        catched.put()
//    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getElementID() {
        return ElementID;
    }

    public void setElementID(int elementID) {
        ElementID = elementID;
    }

    public void setCatchedTime(String catchedTime) {
        CatchedTime = catchedTime;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCatchedTime() {
        return CatchedTime;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public float getLength() {
        return Length;
    }

    public void setLength(float length) {
        Length = length;
    }

    public float getWeight() {
        return Weight;
    }

    public void setWeight(float weight) {
        Weight = weight;
    }


    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
