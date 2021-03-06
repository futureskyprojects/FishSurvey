package me.vistark.coppavietnam.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import me.vistark.coppavietnam.Model.Config.ApplicationConfig;
import me.vistark.coppavietnam.Model.Entity.fsElement;

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

    public boolean isHave(fsElement element) {
        fsElement element1 = getEntry(element.getID());
        if (element1 == null)
            return false;
        else {
            return true;
        }
    }

    public void addEntry(fsElement element) {
        if (!isHave(element)) {
            Log.d("THÊM CÁ", "Không có! thêm mới! " + element.toString());
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put(ID, element.getID());
            c.put(CATEGORIZE_ID, element.getSfCategorizeID());
            c.put(NAME, element.getName());
            c.put(FEATURE_IMAGE_BITMAP, element.getFeatureImageBytes());
            c.put(FEATURE_IMAGE_URL, element.getFeatureImageLink());
            database.insert(TABLE_NAME, null, c);
        } else {
            Log.d("THÊM CÁ", "Đã có! Cập nhật!");
            updateEntry(element);
        }
    }

    public fsElement getEntry(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, this.ID + " = ?", new String[]{String.valueOf(ID)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
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
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return new ArrayList<>();
        try {
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
        } catch (Exception e) {
        }
        return elements;
    }
    public ArrayList<fsElement> getEntriesByCategorizeID(int fsCategorizeID) {
        ArrayList<fsElement> elements = new ArrayList<>();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CATEGORIZE_ID + " = " + fsCategorizeID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return elements;
        while (!cursor.isAfterLast()) {
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

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"",new String[]{});
        db.close();
    }
    public void deleteEntry(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, this.ID + " = ?", new String[] {String.valueOf(ID)});
        db.close();
    }
}
