package com.blogspot.tndev1403.fishSurvey.model;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Captain object
 */

public class Captain {
    /* Default data key for save captain info to SharedPreference */
    private final static String TAG = "Captain";
    private final static String USER_ID = "USER_ID";
    private final static String USER_NAME = "USER_NAME";
    private final static String PHONE_NUMBER = "PHONE_NUMBER";
    private final static String BOAT_CODE = "BOAT_CODE";


    private SharedPreferences sharedPreferences;
    private String UserID;
    private String UserName;
    private String PhoneNumber;
    private String BoatCode;


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public Captain(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        // get captain info
        get();
    }

    public Captain(Context mContext, String userName, String phoneNumber, String boatCode) {
        UserName = userName;
        PhoneNumber = phoneNumber;
        BoatCode = boatCode;
        sharedPreferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        // update captain info
        updateCaptainInfo();
    }

    public boolean get() {
        UserID = sharedPreferences.getString(USER_ID, "");
        UserName = sharedPreferences.getString(USER_NAME, "");
        PhoneNumber = sharedPreferences.getString(PHONE_NUMBER, "");
        BoatCode = sharedPreferences.getString(BOAT_CODE, "");
        return !UserName.isEmpty() && !PhoneNumber.isEmpty() && !BoatCode.isEmpty();
    }

    public boolean updateCaptainInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, UserID);
        editor.putString(USER_NAME, UserName);
        editor.putString(PHONE_NUMBER, PhoneNumber);
        editor.putString(BOAT_CODE, BoatCode);
        return editor.commit();
    }

    public String getUserName() {
        return UserName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getBoatCode() {
        return BoatCode;
    }
}
