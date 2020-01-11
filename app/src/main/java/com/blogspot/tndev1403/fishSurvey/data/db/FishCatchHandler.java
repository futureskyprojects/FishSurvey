package com.blogspot.tndev1403.fishSurvey.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.model.FishCatch;
import com.blogspot.tndev1403.fishSurvey.view.homescreen.HomeScreenPresenter;
import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;

import java.util.ArrayList;
import java.util.Calendar;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Caught fish database
 */

public class FishCatchHandler extends SQLiteOpenHelper {
    private final static String TABLE_NAME = FishCatch.class.getSimpleName();
    private static String ID = "id";
    private static String ELEMENT_ID = "ELEMENT_ID";
    private static String CREATE_DATE = "CREATE_DATE";
    private static String LENGTH = "LENGTH";
    private static String WEIGHT = "WEIGHT";
    private static String CATCH_TIME = "CATCH_TIME";
    private static String LATITUDE = "LATITUDE";
    private static String LONGITUDE = "LONGITUDE";
    private static String IMAGE_PATH = "IMAGE_PATH";
    private static String TRIP_ID = "TRIP_ID";
    private static String FINISHED_TIME = "FINISHED_TIME";

    public FishCatchHandler(Context mContext) {
        super(mContext, Global.DATABASE_NAME + "_" + TABLE_NAME, null, Global.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = String.format("CREATE TABLE IF NOT EXISTS %s(" +
                "%s INTEGER PRIMARY KEY," +
                "%s INTEGER," +
                "%s TEXT," +
                "%s FLOAT," +
                "%s FLOAT," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT)", TABLE_NAME, ID, ELEMENT_ID, CREATE_DATE, LENGTH, WEIGHT, CATCH_TIME, LATITUDE, LONGITUDE, IMAGE_PATH, TRIP_ID, FINISHED_TIME);
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DropTable = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(DropTable);
        onCreate(db);
    }

    // Method for add new caught fish record
    public void add(FishCatch fishCatch) {
        Calendar calendar = Calendar.getInstance();
        String Now = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, fishCatch.getId());
        values.put(ELEMENT_ID, fishCatch.getElementId());
        values.put(CREATE_DATE, Now);
        values.put(LENGTH, fishCatch.getLength());
        values.put(WEIGHT, fishCatch.getWeight());
        values.put(CATCH_TIME, fishCatch.getCatchTime());
        values.put(LATITUDE, fishCatch.getLatitude());
        values.put(LONGITUDE, fishCatch.getLongitude());
        values.put(IMAGE_PATH, fishCatch.getImagePath());
        values.put(TRIP_ID, fishCatch.getTripId());
        values.put(FINISHED_TIME, "");

        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    // Method for count how many record not sync yet
    public int countNotSyncRecords() {
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TRIP_ID + " !=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null || !db.isOpen())
            return -1;
        Cursor cursor = db.rawQuery(Query, new String[]{HomeScreenPresenter.CURRENT_TRIP_ID});
        int res = 0;
        if (cursor != null) {
            res = cursor.getCount();
            cursor.close();
        }
        db.close();
        return res;
    }

    // Method get not sync caught fish. That mean record must have tripId difference with current tripId
    public ArrayList<FishCatch> getNotSyncRecords(String tripID) {
        ArrayList<FishCatch> catcheds = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TRIP_ID + " !=?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null || !db.isOpen())
            return null;
        Cursor cursor = db.rawQuery(Query, new String[]{tripID});
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        while (!cursor.isAfterLast()) {
            FishCatch catched = new FishCatch(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getFloat(3) + "",
                    cursor.getFloat(4) + "",
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10)
            );
            catcheds.add(catched);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return catcheds;
    }

    // Count caught fish in a trip
    public int count(String tripID) {
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TRIP_ID + " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null || !db.isOpen())
            return -1;
        Cursor cursor = db.rawQuery(Query, new String[]{tripID});
        int res = 0;
        if (cursor != null) {
            res = cursor.getCount();
            cursor.close();
        }
        db.close();
        return res;
    }

    // Get all caught fish in trip
    public ArrayList<FishCatch> get(String tripID) {
        ArrayList<FishCatch> catcheds = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null || !db.isOpen())
            return null;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        while (!cursor.isAfterLast()) {
            FishCatch fishCatch = new FishCatch(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getFloat(3) + "",
                    cursor.getFloat(4) + "",
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10)
            );
            if (fishCatch.getTripId().equals(tripID)) {
                catcheds.add(0, fishCatch);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return catcheds;
    }

    // Update all finish time of caught fish record to set this trip was finish
    public int updateFishTime(String tripId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null || !db.isOpen())
            return -1;
        ContentValues c = new ContentValues();
        String t = ProcessingLibrary.Using.GetReverseCurrentDateString();
        c.put(FINISHED_TIME, t);
        int result = db.update(TABLE_NAME, c, TRIP_ID + " = ?", new String[]{tripId});
        db.close();
        return result;
    }

    // Update caught fish data
    public long update(FishCatch fishCatch) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, fishCatch.getId());
        values.put(ELEMENT_ID, fishCatch.getElementId());
        values.put(CREATE_DATE, fishCatch.getCreatedDate());
        values.put(LENGTH, fishCatch.getLength());
        values.put(WEIGHT, fishCatch.getWeight());
        values.put(CATCH_TIME, fishCatch.getCatchTime());
        values.put(LATITUDE, fishCatch.getLatitude());
        values.put(LONGITUDE, fishCatch.getLongitude());
        values.put(IMAGE_PATH, fishCatch.getImagePath());
        values.put(TRIP_ID, fishCatch.getTripId());
        values.put(CATCH_TIME, fishCatch.getCatchTime());

        int res = db.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(fishCatch.getId())});
        db.close();
        return res;
    }

    // Delete fish data
    public long deleteEntry(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_NAME, FishCatchHandler.ID + " = ?", new String[]{String.valueOf(ID)});
        db.close();
        return res;
    }
}
