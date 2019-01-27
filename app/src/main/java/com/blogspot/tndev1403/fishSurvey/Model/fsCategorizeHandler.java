package com.blogspot.tndev1403.fishSurvey.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCategorize;

import java.util.ArrayList;

public class fsCategorizeHandler extends SQLiteOpenHelper {
    public final static String TABLE_NAME = "categorize";

    public final static String ID = "ID";
    public final static String NAME = "Name";
    public final static String FeatureImageBitmap = "FeatureImageBitmap";
    public final static String FeatureImageURL = "FeatureImageURL";

    public fsCategorizeHandler(Context mContext) {
        super(mContext, ApplicationConfig.DATABASE_NAME, null, ApplicationConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = String.format("CREATE TABLE %s(" +
                "%s INTEGER PRIMARY KEY," +
                "%s TEXT," +
                "%s BLOB," +
                "%s TEXT)", TABLE_NAME, ID, NAME, FeatureImageBitmap, FeatureImageURL);
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DropTable = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(DropTable);
        onCreate(db);
    }

    public void addEntry(fsCategorize categorize) throws Exception {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(ID, categorize.getID());
        c.put(NAME, categorize.getName());
        c.put(FeatureImageBitmap, categorize.getFeatureImageBytes());
        c.put(FeatureImageURL, categorize.getFeatureImageLink());
        database.insert(TABLE_NAME, null, c);
        database.close();
    }

    public fsCategorize getEntry(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, this.ID + " = ?", new String[]{String.valueOf(ID)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        fsCategorize categorize = new fsCategorize(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getBlob(2),
                cursor.getString(3)
        );
        return categorize;
    }

    public ArrayList<fsCategorize> getAllEntry() {
        ArrayList<fsCategorize> categorizes = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        while (cursor.isAfterLast() == false) {
            fsCategorize categorize = new fsCategorize(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getBlob(2),
                    cursor.getString(3)
            );
            categorizes.add(categorize);
            cursor.moveToNext();
        }
        return categorizes;
    }

    public void updateEntry(fsCategorize categorize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(ID, categorize.getID());
        c.put(NAME, categorize.getName());
        c.put(FeatureImageBitmap, categorize.getFeatureImageBytes());
        c.put(FeatureImageURL, categorize.getFeatureImageLink());
        db.update(TABLE_NAME, c, this.ID  + " = ?", new String[]{String.valueOf(categorize.getID())});
        db.close();
    }

    public void deleteEntry(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, this.ID + " = ?", new String[] {String.valueOf(ID)});
        db.close();
    }
}
