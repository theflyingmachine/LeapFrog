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
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_LAT = "latitude";
    public static final String COLUMN_NAME_LONG = "longitude";
    public static final String COLUMN_NAME_TASK = "task";
    public static final String COLUMN_NAME_TODO = "todo";
    public static final String COLUMN_NAME_RADIUS = "radius";
    public static final String COLUMN_NAME_DATE = "date";

   public locationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "   //index 0
                + COLUMN_NAME_LAT + " TEXT,"//index 1
                + COLUMN_NAME_LONG + " TEXT,"//index 2
                + COLUMN_NAME_TASK + " TEXT,"//index 3
                + COLUMN_NAME_TODO + " TEXT,"//index 4
                + COLUMN_NAME_RADIUS + " INTEGER,"//index 5
                + COLUMN_NAME_DATE + " TEXT )";//index 6
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertLocation(String lat, String longi, String task, String todo, Integer radius, String date){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_LAT, lat);
        values.put(COLUMN_NAME_LONG, longi);
        values.put(COLUMN_NAME_TASK, task);
        values.put(COLUMN_NAME_TODO, todo);
        values.put(COLUMN_NAME_RADIUS, radius);
        values.put(COLUMN_NAME_DATE, date);


        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
       // db.close(); // Closing database connection
    }

    public String[] displayLocation() {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] reminderdata = new String[1000];
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
        String[] lat = new String[500];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lat[i]=cursor.getString(1);
                i++;
            } while (cursor.moveToNext());
        }
        return lat;
    }


    public String[] displayLong() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] longi = new String[500];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                longi[i]=cursor.getString(2);
                i++;
            } while (cursor.moveToNext());
        }
        return longi;
    }



    public String[] displayTask() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] task = new String[500];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                task[i]=cursor.getString(3);
                i++;
            } while (cursor.moveToNext());
        }
        return task;
    }


    public String[] displayId() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] id = new String[500];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id[i]=cursor.getString(0);
                i++;
            } while (cursor.moveToNext());
        }
        return id;
    }


    public String[] displayTodo() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] todo = new String[500];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                todo[i]=cursor.getString(4);
                i++;
            } while (cursor.moveToNext());
        }
        return todo;
    }
}

