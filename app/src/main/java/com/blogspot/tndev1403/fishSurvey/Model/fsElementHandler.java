package com.blogspot.tndev1403.fishSurvey.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;

import java.util.ArrayList;

public class fsElementHandler extends SQLiteOpenHelper {
    public final static String TABLE_NAME = "element";

    public final static String ID = "ID";
    public final static String NAME = "NAME";
    public final static String FEATURE_IMAGE_BITMAP = "FEATURE_IMAGE_BITMAP";
    public final static String FEATURE_IMAGE_URL = "FEATURE_IMAGE_URL";
    public final static String CATEGORIZE_ID = "CATEGORIZE_ID";

    public fsElementHandler(Context mContext) {
        super(mContext, ApplicationConfig.DATABASE_NAME, null, ApplicationConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = String.format("CREATE TABLE %s(" +
                "%s INTEGER PRIMARY KEY," +
                "%s INTEGER," +
                "%s TEXT," +
                "%s BLOB," +
                "%s TEXT)", TABLE_NAME, ID, CATEGORIZE_ID, NAME, FEATURE_IMAGE_BITMAP, FEATURE_IMAGE_URL);
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DropTable = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(DropTable);
        onCreate(db);
    }

    public void addEntry(fsElement element) throws Exception {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(ID, element.getID());
        c.put(CATEGORIZE_ID, element.getSfCategorizeID());
        c.put(NAME, element.getName());
        c.put(FEATURE_IMAGE_BITMAP, element.getFeatureImageBytes());
        c.put(FEATURE_IMAGE_URL, element.getFeatureImageLink());
        database.insert(TABLE_NAME, null, c);
        database.close();
    }

    public fsElement getEntry(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, this.ID + " = ?", new String[]{String.valueOf(ID)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        fsElement element = new fsElement(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getBlob(3),
                cursor.getString(4)
        );
        return element;
    }

    public ArrayList<fsElement> getAllEntry() {
        ArrayList<fsElement> elements = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        while (cursor.isAfterLast() == false) {
            fsElement element = new fsElement(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getBlob(3),
                    cursor.getString(4)
            );
            elements.add(element);
            cursor.moveToNext();
        }
        return elements;
    }

    public void updateEntry(fsElement element) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(ID, element.getID());
        c.put(CATEGORIZE_ID, element.getSfCategorizeID());
        c.put(NAME, element.getName());
        c.put(FEATURE_IMAGE_BITMAP, element.getFeatureImageBytes());
        c.put(FEATURE_IMAGE_URL, element.getFeatureImageLink());
        db.update(TABLE_NAME, c, this.ID  + " = ?", new String[]{String.valueOf(element.getID())});
        db.close();
    }

    public void deleteEntry(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, this.ID + " = ?", new String[] {String.valueOf(ID)});
        db.close();
    }
}
