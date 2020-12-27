package me.vistark.coppa_v2.Model.Entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class fsUser {
    public final static String TAG = "fsUser";
    public final static String USER_ID = "USER_ID";
    public final static String USER_NAME = "USER_NAME";
    public final static String PHONE_NUMBER = "PHONE_NUMBER";
    public final static String BOAT_CODE = "BOAT_CODE";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context mContext;
    String UserID;
    String UserName;
    String PhoneNumber;
    String BoatCode;

    @Override
    public String toString() {
        return "Người dùng mang mã" + UserID + " có tên: " + UserName + "\n" +
                "Số điện thoại: " + PhoneNumber + "\n" +
                "Mã tàu: " + BoatCode + "\n";
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        Log.e(TAG, "setUserID: <140398> to " + userID);
        UserID = userID;
    }

    public fsUser(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        get();
    }

    public fsUser(Context mContext, String userName, String phoneNumber, String boatCode) {
        this.mContext = mContext;
        UserName = userName;
        PhoneNumber = phoneNumber;
        BoatCode = boatCode;
        sharedPreferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        commit();
    }

    public boolean get() {
        UserID = sharedPreferences.getString(USER_ID, "");
        UserName = sharedPreferences.getString(USER_NAME, "");
        PhoneNumber = sharedPreferences.getString(PHONE_NUMBER, "");
        BoatCode = sharedPreferences.getString(BOAT_CODE, "");
        if (UserName.isEmpty() || PhoneNumber.isEmpty() || BoatCode.isEmpty())
            return false;
        else
            return true;
    }

    public boolean commit() {
        editor.putString(USER_ID, UserID);
        editor.putString(USER_NAME, UserName);
        editor.putString(PHONE_NUMBER, PhoneNumber);
        editor.putString(BOAT_CODE, BoatCode);
        return editor.commit();
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getBoatCode() {
        return BoatCode;
    }

    public void setBoatCode(String boatCode) {
        BoatCode = boatCode;
    }

}
