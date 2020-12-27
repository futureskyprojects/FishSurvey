package me.vistark.coppa_v2.Model.Entity;

import android.content.Context;
import android.content.SharedPreferences;

public class fsCustomSetting {
    public final static String TAG = "fsCustomSetting";
    public final static String IS_FIST_START = "IS_FISRT_START";
    Context mContext;
    boolean isFistStart;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public fsCustomSetting(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isFistStart = sharedPreferences.getBoolean(IS_FIST_START, true);
    }
    public boolean started() {
        editor.putBoolean(IS_FIST_START, false);
        return editor.commit();
    }
}
