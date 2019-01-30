package com.blogspot.tndev1403.fishSurvey.Model.Entity;

import com.blogspot.tndev1403.fishSurvey.TNLib;

import org.json.JSONObject;

import java.util.Date;

public class fsCatched {
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
    public fsCatched(int ID, int elementID, String createdDate, float length, float weight, String catchedTime, String latitude, String longitude, String imagePath) {
        this.ID = ID;
        ElementID = elementID;
        CreatedDate = TNLib.Using.StringToDateTime(createdDate);
        Length = length;
        Weight = weight;
        CatchedTime = TNLib.Using.StringToDateTime(catchedTime);
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

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
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

    public Date getCatchedTime() {
        return CatchedTime;
    }

    public void setCatchedTime(Date catchedTime) {
        CatchedTime = catchedTime;
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
