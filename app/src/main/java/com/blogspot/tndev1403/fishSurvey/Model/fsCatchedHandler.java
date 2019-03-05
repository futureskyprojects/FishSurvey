package com.blogspot.tndev1403.fishSurvey.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCatched;
import com.blogspot.tndev1403.fishSurvey.TNLib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class fsCatchedHandler extends SQLiteOpenHelper {
    public final static String TABLE_NAME = "Catched";
    public static String ID = "ID";
    public static String ELEMENT_ID = "ELEMENT_ID";
    public static String CREATEED_DATE = "CREATEED_DATE";
    public static String LENGTH = "LENGTH";
    public static String WEIGHT = "WEIGHT";
    public static String CATCHED_TIME = "CATCHED_TIME";
    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";
    public static String IMAGE_PATH = "IMAGE_PATH";
    public static String TRIP_ID = "TRIP_ID";
    public static String FINISHED_TIME = "FINISHED_TIME";

    public fsCatchedHandler(Context mContext) {
        super(mContext, ApplicationConfig.DATABASE_NAME + "_Catched", null, ApplicationConfig.DATABASE_VERSION);
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
                "%s TEXT)", TABLE_NAME, ID, ELEMENT_ID, CREATEED_DATE, LENGTH, WEIGHT, CATCHED_TIME, LATITUDE, LONGITUDE, IMAGE_PATH, TRIP_ID, FINISHED_TIME);
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DropTable = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(DropTable);
        onCreate(db);
    }

    public int getMAXID() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT MAX(" + ID + ") FROM " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else
            return -1;
        return cursor.getInt(0);
    }

    public void addEntry(fsCatched catched) throws Exception {
        //---------------
        Calendar calendar = Calendar.getInstance();
        String Now = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        //-----------------
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(ID, catched.getID());
        c.put(ELEMENT_ID, catched.getElementID());
        c.put(CREATEED_DATE, Now);
        c.put(LENGTH, catched.getLength());
        c.put(WEIGHT, catched.getWeight());
        c.put(CATCHED_TIME, catched.getCatchedTime());
        c.put(LATITUDE, catched.getLatitude());
        c.put(LONGITUDE, catched.getLongitude());
        c.put(IMAGE_PATH, catched.getImagePath());
        c.put(TRIP_ID, catched.getTrip_id());
        c.put(FINISHED_TIME, "");
        database.insert(TABLE_NAME, null, c);
        database.close();
    }

    public fsCatched getEntry(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, this.ID + " = ?", new String[]{String.valueOf(ID)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        fsCatched catched = new fsCatched(
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
        return catched;
    }

    public ArrayList<fsCatched> getAllEntryDifferentWithCurrentTripID(String Trip_ID) {
        ArrayList<fsCatched> catcheds = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        while (cursor.isAfterLast() == false) {
            fsCatched catched = new fsCatched(
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
            if (!catched.getTrip_id().equals(Trip_ID))
                catcheds.add(catched);
            cursor.moveToNext();
        }
        return catcheds;
    }

    public int CountAllEntry(String Trip_ID) {
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TRIP_ID + " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, new String[] {Trip_ID});
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }
    public ArrayList<fsCatched> getAllEntry(String Trip_ID) {
        ArrayList<fsCatched> catcheds = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        while (cursor.isAfterLast() == false) {
            fsCatched catched = new fsCatched(
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
            if (catched.getTrip_id().equals(Trip_ID))
            {
                catcheds.add(0, catched);
            }
            cursor.moveToNext();
        }
        return catcheds;
    }

    public int UpdateAllFinishedTimeOfATrip(String trip_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(FINISHED_TIME, TNLib.Using.GetReverseCurrentDateString());
        int x = db.update(TABLE_NAME, c, this.TRIP_ID + " = ?", new String[]{trip_id});
        db.close();
        return x;
    }

    public void updateEntry(fsCatched catched) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(ID, catched.getID());
        c.put(ELEMENT_ID, catched.getElementID());
        c.put(CREATEED_DATE, catched.getCreatedDate());
        c.put(LENGTH, catched.getLength());
        c.put(WEIGHT, catched.getWeight());
        c.put(CATCHED_TIME, catched.getCatchedTime());
        c.put(LATITUDE, catched.getLatitude());
        c.put(LONGITUDE, catched.getLongitude());
        c.put(IMAGE_PATH, catched.getImagePath());
        c.put(TRIP_ID, catched.getTrip_id());
        c.put(CATCHED_TIME, catched.getCatchedTime());
        db.update(TABLE_NAME, c, this.ID + " = ?", new String[]{String.valueOf(catched.getID())});
        db.close();
    }

    public void deleteEntry(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, this.ID + " = ?", new String[]{String.valueOf(ID)});
        db.close();
    }
}
