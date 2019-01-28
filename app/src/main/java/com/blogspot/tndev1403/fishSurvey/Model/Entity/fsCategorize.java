package com.blogspot.tndev1403.fishSurvey.Model.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.TNLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class fsCategorize {
    public final static String TAG = "fsCategorizePresenter";
    int ID;
    String Name;
    Bitmap FeatureImage;
    String FeatureImageLink;

    //region For JSON read
    public static ArrayList<fsCategorize> getFromAPI() {
        ArrayList<fsCategorize> categorizes = new ArrayList<>();
        /* Download json String */
        String jsonText = TNLib.Using.getContent(ApplicationConfig.CategorizeAPI.URL);
        try {
            JSONObject Jobj = new JSONObject(jsonText);
            JSONArray Jarr = Jobj.getJSONArray(ApplicationConfig.CategorizeAPI.RootKey);
            for (int i = 0; i < Jarr.length(); i++) {
                fsCategorize categorize = new fsCategorize(Jarr.getJSONObject(i));
                if (categorize.FeatureImage != null)
                    categorizes.add(categorize);
            }
        } catch (Exception e) {
            Log.e(TAG, "getFromAPI: " + e.getMessage());
        }
        return categorizes;
    }

    public fsCategorize(JSONObject jsonCategorize) {
        try {
            this.ID = jsonCategorize.getInt(ApplicationConfig.CategorizeAPI.ID);
            this.Name = jsonCategorize.getString(ApplicationConfig.CategorizeAPI.Name);
            this.FeatureImageLink = jsonCategorize.getString(ApplicationConfig.CategorizeAPI.FeatureImage).replace("https://", "http://");
            this.FeatureImage = TNLib.Using.getBitmap(this.FeatureImageLink);
        } catch (Exception e) {

        }
    }

    public fsCategorize(String jsonText) {
        try {
            JSONObject jsonCategorize = new JSONObject(jsonText);
            this.ID = jsonCategorize.getInt(ApplicationConfig.CategorizeAPI.ID);
            this.Name = jsonCategorize.getString(ApplicationConfig.CategorizeAPI.Name);
            this.FeatureImageLink = jsonCategorize.getString(ApplicationConfig.CategorizeAPI.FeatureImage);
            this.FeatureImage = TNLib.Using.getBitmap(this.FeatureImageLink);
        } catch (Exception e) {
        }
    }
    //endregion

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
