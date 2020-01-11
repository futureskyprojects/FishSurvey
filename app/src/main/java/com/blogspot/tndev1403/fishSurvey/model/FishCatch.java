package com.blogspot.tndev1403.fishSurvey.model;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Define object for interactive with fish was caught
 */

public class FishCatch {
    private int id;
    private int elementId;
    private String createdDate;
    private String length;
    private String weight;
    private String catchTime;
    private String latitude;
    private String longitude;
    private String imagePath;
    private String tripId;
    private String finishTime;

    public FishCatch(int id, int elementId, String createdDate, String length, String weight, String catchedTime, String latitude, String longitude, String imagePath, String tripId, String finishedTime) {
        this.id = id;
        this.elementId = elementId;
        this.createdDate = createdDate;
        this.length = length;
        this.weight = weight;
        this.catchTime = catchedTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = imagePath;
        this.tripId = tripId;
        this.finishTime = finishedTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getTripId() {
        return tripId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getElementId() {
        return elementId;
    }

    public void setCatchTime(String catchTime) {
        this.catchTime = catchTime;
    }

    public String getCatchTime() {
        return catchTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
