package com.blogspot.tndev1403.fishSurvey.Model.Entity;

import android.graphics.Bitmap;

public class fsCatchedPreview {
    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public fsCatchedPreview(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
