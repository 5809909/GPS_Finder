package com.github.a5809909.gps_finder.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.a5809909.gps_finder.Model.LocationModel;

public class DatabaseHelper extends SQLiteOpenHelper {   // Database Version

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LocationDataBase.db";

    private static final String TABLE_NAME = "LocationTable";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY_AND_TIME = "dayAndTime";
    public static final String COLUMN_CELL_ID = "cellId";
    public static final String COLUMN_LAC = "lac";
    public static final String COLUMN_MCC = "mcc";
    public static final String COLUMN_MNC = "mnc";
    public static final String COLUMN_JSON_STRING = "jsonString";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_ACCURACY = "accuracy";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHOTOS = "urlPhotos";

    private String CREATE_PHONE_STATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_DAY_AND_TIME + " TEXT," +
            COLUMN_CELL_ID + " TEXT," +
            COLUMN_LAC + " TEXT," +
            COLUMN_MCC + " TEXT," +
            COLUMN_MNC + " TEXT," +
            COLUMN_JSON_STRING + " TEXT," +
            COLUMN_LAT + " TEXT," +
            COLUMN_LNG + " TEXT," +
            COLUMN_ACCURACY + " TEXT," +
            COLUMN_ADDRESS + " TEXT," +
            COLUMN_PHOTOS + " TEXT" + ")";

    private String DROP_PHONE_STATE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PHONE_STATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_PHONE_STATE_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void addUser(LocationModel pLocationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY_AND_TIME, pLocationModel.getDateAndTime());
        values.put(COLUMN_CELL_ID, pLocationModel.getCellId());
        values.put(COLUMN_LAC, pLocationModel.getLac());
        values.put(COLUMN_MCC, pLocationModel.getMcc());
        values.put(COLUMN_MNC, pLocationModel.getMnc());
        values.put(COLUMN_JSON_STRING, pLocationModel.getJson_first());
        values.put(COLUMN_LAT, pLocationModel.getLat());
        values.put(COLUMN_LNG, pLocationModel.getLng());
        values.put(COLUMN_ACCURACY, pLocationModel.getAcc());
        values.put(COLUMN_ADDRESS, pLocationModel.getAddress());
        values.put(COLUMN_PHOTOS, pLocationModel.getUrlPhotos());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();

        Log.i(TAG, "addUser: ");
    }

    public Cursor getLastItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sortOrder =
                COLUMN_DAY_AND_TIME + " DESC";
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, sortOrder);
        Log.i(TAG, "getLastItem: DESC" + cursor.getPosition());
        return cursor;
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sortOrder =
                COLUMN_DAY_AND_TIME + " DESC";
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, sortOrder);
        cursor.moveToLast();
        Log.i(TAG, "getAllItems: " + cursor.getPosition());
        return cursor;
    }

    public LocationModel getAllLocationModels() {
        LocationModel pLocationModel = new LocationModel();

        String[] columns = {
                COLUMN_ID,
                COLUMN_DAY_AND_TIME,
                COLUMN_CELL_ID,
                COLUMN_LAC,
                COLUMN_MCC,
                COLUMN_MNC,
                COLUMN_LAT,
                COLUMN_LNG,
                COLUMN_ACCURACY,
                COLUMN_ADDRESS,
                COLUMN_PHOTOS
        };
        String sortOrder =
                COLUMN_DAY_AND_TIME + " DESC limit 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        cursor.moveToLast();
        try {
            pLocationModel.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
            pLocationModel.setDateAndTime(cursor.getString(cursor.getColumnIndex(COLUMN_DAY_AND_TIME)));
            pLocationModel.setCellId(cursor.getString(cursor.getColumnIndex(COLUMN_CELL_ID)));
            pLocationModel.setLac(cursor.getString(cursor.getColumnIndex(COLUMN_LAC)));
            pLocationModel.setMcc(cursor.getString(cursor.getColumnIndex(COLUMN_MCC)));
            pLocationModel.setMnc(cursor.getString(cursor.getColumnIndex(COLUMN_MNC)));
            pLocationModel.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LAT)));
            pLocationModel.setLng(cursor.getString(cursor.getColumnIndex(COLUMN_LNG)));
            pLocationModel.setAcc(cursor.getString(cursor.getColumnIndex(COLUMN_ACCURACY)));
            pLocationModel.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)));
            pLocationModel.setUrlPhotos(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTOS)));

        } catch (Exception e) {
        } finally {
            cursor.close();
            db.close();
        }
        // return pLocationModel list
        return pLocationModel;
    }

    public void updateUser(LocationModel pLocationModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, pLocationModel.getLat());
        values.put(COLUMN_LNG, pLocationModel.getLng());
        values.put(COLUMN_ACCURACY, pLocationModel.getAcc());
        values.put(COLUMN_ADDRESS, pLocationModel.getAddress());

        // updating row
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(pLocationModel.get_id())});
        db.close();
    }

    public void deleteIditem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete pLocationModel record by id
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{id});
        db.close();
    }
}
