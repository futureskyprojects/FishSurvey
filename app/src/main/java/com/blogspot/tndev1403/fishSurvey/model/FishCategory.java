package com.blogspot.tndev1403.fishSurvey.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Fish category for classify fish type
 */

public class FishCategory {
    private int id;
    private String name;
    private Bitmap featureImage;
    private String featureImageLink;

    FishCategory(int id, String name, Bitmap featureImage, String featureImageLink) {
        this.id = id;
        this.name = name;
        this.featureImage = featureImage;
        this.featureImageLink = featureImageLink;
    }

    FishCategory(int id, String name, byte[] featureImageBytes, String featureImageLink) {
        this.id = id;
        this.name = name;
        featureImage = BitmapFactory.decodeByteArray(featureImageBytes, 0, featureImageBytes.length);
        this.featureImageLink = featureImageLink;
    }

    public String getFeatureImageLink() {
        return featureImageLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getFeatureImage() {
        return featureImage;
    }

    public byte[] getFeatureImageBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        featureImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void setFeatureImage(Bitmap featureImage) {
        this.featureImage = featureImage;
    }
}
