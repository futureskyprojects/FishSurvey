package com.blogspot.tndev1403.fishSurvey.Model.Entity;
 import android.graphics.Bitmap;
import android.util.Log;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.TNLib;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class fsElement extends fsCategorize{
    int sfCategorizeID;

    public fsElement(int ID, int sfCategorizeID, String name, byte[] featureImageBytes, String FeatureImageLink) {
        super(ID, name, featureImageBytes, FeatureImageLink);
        this.sfCategorizeID = sfCategorizeID;
    }

    //region For JSON read
    public static ArrayList<fsElement> getFromAPI(String categorize_id) {
        ArrayList<fsElement> elements = new ArrayList<>();
        /* Download json String */
        String jsonText = TNLib.Using.getContent(ApplicationConfig.ElementAPI.URL + categorize_id);
        try {
            JSONObject Jobj = new JSONObject(jsonText);
            JSONArray Jarr = Jobj.getJSONArray(ApplicationConfig.ElementAPI.RootKey);
            for (int i = 0; i < Jarr.length(); i++) {
                fsElement element = new fsElement(Jarr.getJSONObject(i));
//                if (element.FeatureImage != null)
                elements.add(element);
            }
        } catch (Exception e) {
            Log.e(TAG, "getFromAPI: " + e.getMessage());
        }
        return elements;
    }

    public fsElement(JSONObject jsonElements) {
        try {
            this.ID = jsonElements.getInt(ApplicationConfig.ElementAPI.ID);
            this.Name = jsonElements.getString(ApplicationConfig.ElementAPI.Name);
            this.FeatureImageLink = jsonElements.getString(ApplicationConfig.ElementAPI.FeatureImage).replace("https://", "http://");
            this.FeatureImage = TNLib.Using.getBitmap(this.FeatureImageLink);
            this.sfCategorizeID = jsonElements.getInt(ApplicationConfig.ElementAPI.fsCategorizeID);
        } catch (Exception e) {

        }
    }

    public fsElement(String jsonText) {
        try {
            JSONObject jsonElements = new JSONObject(jsonText);
            this.ID = jsonElements.getInt(ApplicationConfig.ElementAPI.ID);
            this.Name = jsonElements.getString(ApplicationConfig.ElementAPI.Name);
            this.FeatureImageLink = jsonElements.getString(ApplicationConfig.ElementAPI.FeatureImage).replace("https://", "http://");
            this.FeatureImage = TNLib.Using.getBitmap(this.FeatureImageLink);
            this.sfCategorizeID = jsonElements.getInt(ApplicationConfig.ElementAPI.fsCategorizeID);
        } catch (Exception e) {
        }
    }

    //endregion
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
