package com.blogspot.tndev1403.fishSurvey.Model.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class fsCategorize {
    public final static String TAG = "fsCategorizeActivity";
    int ID;
    String Name;
    Bitmap FeatureImage;
    String FeatureImageLink;

    public fsCategorize(int ID, String name, Bitmap featureImage, String FeatureImageLink) {
        this.ID = ID;
        Name = name;
        FeatureImage = featureImage;
        this.FeatureImageLink = FeatureImageLink;
    }
    public fsCategorize(int ID, String name, byte[] featureImageBytes, String FeatureImageLink) {
        this.ID = ID;
        Name = name;
        FeatureImage = BitmapFactory.decodeByteArray(featureImageBytes, 0, featureImageBytes.length);
        this.FeatureImageLink = FeatureImageLink;
    }

    public String getFeatureImageLink() {
        return FeatureImageLink;
    }

    public void setFeatureImageLink(String featureImageLink) {
        FeatureImageLink = featureImageLink;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Bitmap getFeatureImage() {
        return FeatureImage;
    }

    public byte[] getFeatureImageBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        FeatureImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void setFeatureImage(Bitmap featureImage) {
        FeatureImage = featureImage;
    }
}
