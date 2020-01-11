package com.blogspot.tndev1403.fishSurvey.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.tndev1403.fishSurvey.data.config.Global;
import com.blogspot.tndev1403.fishSurvey.model.Fish;

import java.util.ArrayList;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Fish storage database
 */

public class FishHandler extends SQLiteOpenHelper {
    private final static String TABLE_NAME = Fish.class.getSimpleName();

    private final static String ID = "ID";
    private final static String NAME = "NAME";
    private final static String FEATURE_IMAGE_BITMAP = "FEATURE_IMAGE_BITMAP";
    private final static String FEATURE_IMAGE_URL = "FEATURE_IMAGE_URL";
    private final static String CATEGORY_ID = "CATEGORY_ID";

    public FishHandler(Context mContext) {
        super(mContext, Global.DATABASE_NAME + "_" + TABLE_NAME, null, Global.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = String.format("CREATE TABLE %s(" +
                "%s INTEGER PRIMARY KEY," +
                "%s INTEGER," +
                "%s TEXT," +
                "%s BLOB," +
                "%s TEXT)", TABLE_NAME, ID, CATEGORY_ID, NAME, FEATURE_IMAGE_BITMAP, FEATURE_IMAGE_URL);
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DropTable = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(DropTable);
        onCreate(db);
    }

    // Check fish is have or not
    private boolean isHave(Fish fish) {
        Fish element1 = get(fish.getId());
        return element1 != null;
    }

    // Add new fish
    public void add(Fish element) {
        if (!isHave(element)) {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ID, element.getId());
            values.put(CATEGORY_ID, element.getFishCategoryId());
            values.put(NAME, element.getName());
            values.put(FEATURE_IMAGE_BITMAP, element.getFeatureImageBytes());
            values.put(FEATURE_IMAGE_URL, element.getFeatureImageLink());

            database.insert(TABLE_NAME, null, values);

            database.close();
        } else {
            update(element);
        }
    }

    // Method for get fish by ID
    public Fish get(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, FishHandler.ID + " = ?", new String[]{String.valueOf(ID)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;
        Fish fish = new Fish(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getBlob(3),
                cursor.getString(4)
        );
        cursor.close();
        db.close();
        return fish;
    }

    // Method for get all fishes
    public ArrayList<Fish> get() {
        ArrayList<Fish> elements = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return new ArrayList<>();
        try {
            while (!cursor.isAfterLast()) {
                Fish element = new Fish(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getBlob(3),
                        cursor.getString(4)
                );
                elements.add(element);
                cursor.moveToNext();
            }
        } catch (Exception ignore) {
        }
        cursor.close();
        db.close();
        return elements;
    }

    // Get fish by category
    public ArrayList<Fish> getByCategory(int fishCategoryId) {
        ArrayList<Fish> elements = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CATEGORY_ID + " = " + fishCategoryId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return elements;
        while (!cursor.isAfterLast()) {
            Fish element = new Fish(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getString(4)
            );
            elements.add(element);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return elements;
    }

    // Method for update fish record
    private void update(Fish fish) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, fish.getId());
        values.put(CATEGORY_ID, fish.getFishCategoryId());
        values.put(NAME, fish.getName());
        values.put(FEATURE_IMAGE_BITMAP, fish.getFeatureImageBytes());
        values.put(FEATURE_IMAGE_URL, fish.getFeatureImageLink());
        db.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(fish.getId())});
        db.close();
    }

    // Method for delete all fish record
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "", new String[]{});
        db.close();
    }

}
