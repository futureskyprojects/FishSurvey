package com.blogspot.tndev1403.fishSurvey.model;

import android.graphics.Bitmap;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Fish in a category classify category id
 */

public class Fish extends FishCategory {
    private int fishCategoryId;

    public Fish(int id, int fishCategoryId, String name, byte[] featureImageBytes, String featureImageLink) {
        super(id, name, featureImageBytes, featureImageLink);
        this.fishCategoryId = fishCategoryId;
    }

    public Fish(int id, int fishCategoryId, String name, Bitmap featureImage, String featureImageLink) {
        super(id, name, featureImage, featureImageLink);
        this.fishCategoryId = fishCategoryId;
    }

    public int getFishCategoryId() {
        return fishCategoryId;
    }
}
