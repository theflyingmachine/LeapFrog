package com.ericabraham.leapfrog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Eric Abraham on 12-Sep-17.
 */

public class locationDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "leapfrog";
    public static final String TABLE_NAME = "reminder";
    public static final String COLUMN_NAME_LAT = "latitude";
    public static final String COLUMN_NAME_LONG = "longitude";
    public static final String COLUMN_NAME_TASK = "task";
   public locationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_NAME_LAT + " TEXT," + COLUMN_NAME_LONG + " TEXT," + COLUMN_NAME_TASK + " TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertLocation(String lat, String longi){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_LAT, lat); // Contact Name
        values.put(COLUMN_NAME_LONG, longi); // Contact Phone
        values.put(COLUMN_NAME_TASK, "I am a task"); // Contact Phone

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
       // db.close(); // Closing database connection
    }

    public String[] displayLocation() {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] reminderdata = new String[50];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                reminderdata[i]=cursor.getString(0);
                i++;
                reminderdata[i]=cursor.getString(1);
                i++;
                reminderdata[i] = cursor.getString(2);
                i++;
            } while (cursor.moveToNext());
        }
        return reminderdata;
    }


    public String[] displayLat() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] lat = new String[50];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lat[i]=cursor.getString(0);
                i++;
            } while (cursor.moveToNext());
        }
        return lat;
    }


    public String[] displayLong() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] longi = new String[50];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                longi[i]=cursor.getString(1);
                i++;
            } while (cursor.moveToNext());
        }
        return longi;
    }



    public String[] displayTask() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] task = new String[50];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                task[i]=cursor.getString(2);
                i++;
            } while (cursor.moveToNext());
        }
        return task;
    }



}

