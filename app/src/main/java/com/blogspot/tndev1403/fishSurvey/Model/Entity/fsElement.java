package com.blogspot.tndev1403.fishSurvey.Model.Entity;

import android.graphics.Bitmap;

public class fsElement extends fsCategorize {
    int sfCategorizeID;

    public fsElement(int ID, int sfCategorizeID, String name, byte[] featureImageBytes, String FeatureImageLink) {
        super(ID, name, featureImageBytes, FeatureImageLink);
        this.sfCategorizeID = sfCategorizeID;
    }

    public fsElement(int ID, int fsCategorizeID, String name, Bitmap featureImage, String featureImageLink) {
        super(ID, name, featureImage, featureImageLink);
        this.sfCategorizeID = fsCategorizeID;
    }

    public int getSfCategorizeID() {
        return sfCategorizeID;
    }

    public void setSfCategorizeID(int sfCategorizeID) {
        this.sfCategorizeID = sfCategorizeID;
    }
}
