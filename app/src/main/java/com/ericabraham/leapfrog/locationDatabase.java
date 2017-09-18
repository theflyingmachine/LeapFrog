package com.ericabraham.leapfrog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Eric Abraham on 12-Sep-17.
 */

public class locationDatabase extends SQLiteOpenHelper {

    int rowCount=0;
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
    public static final String COLUMN_NAME_NAME = "pname";
    public static final String COLUMN_NAME_ADDRESS = "paddress";

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
                + COLUMN_NAME_DATE + " TEXT,"//index 6
                + COLUMN_NAME_NAME + " TEXT,"//index 7
                + COLUMN_NAME_ADDRESS + " TEXT )";//index 8

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertLocation(String lat, String longi, String task, String todo, Integer radius, String date, String pname, String address){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_LAT, lat);
        values.put(COLUMN_NAME_LONG, longi);
        values.put(COLUMN_NAME_TASK, task);
        values.put(COLUMN_NAME_TODO, todo);
        values.put(COLUMN_NAME_RADIUS, radius);
        values.put(COLUMN_NAME_DATE, date);
        values.put(COLUMN_NAME_NAME, pname);
        values.put(COLUMN_NAME_ADDRESS, address);


        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
       // db.close(); // Closing database connection
    }

    public String[] displayLocation() {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] reminderdata = new String[100];
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
        String[] lat = new String[rowCount];
        int i = 0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lat[i] = cursor.getString(1);
                i++;
            } while (cursor.moveToNext());
        }
        return lat;
    }

        public String[] displayLong() {
            String selectQuery = "SELECT * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            String[] longi = new String[rowCount];
            int i = 0;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    longi[i] = cursor.getString(2);
                    i++;
                } while (cursor.moveToNext());
            }
            return longi;
        }

    public String[] displayTask() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] task = new String[rowCount];
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

    public String[] displayName() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] name = new String[rowCount];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name[i]=cursor.getString(7);
                i++;
            } while (cursor.moveToNext());
        }
        return name;
    }

    public int[] displayAllRadius() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int[] rad = new int[rowCount];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                rad[i]=cursor.getInt(5);
                i++;
            } while (cursor.moveToNext());
        }
        return rad;
    }


    public String[] displayDate() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] date = new String[rowCount];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                date[i]=cursor.getString(6);
                i++;
            } while (cursor.moveToNext());
        }
        return date;
    }


    public String[] displayAddress() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] add = new String[rowCount];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                add[i]=cursor.getString(8);
                i++;
            } while (cursor.moveToNext());
        }
        return add;
    }




    public int[] displayId() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int[] id = new int[rowCount];
        int i=0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id[i]=Integer.parseInt(cursor.getString(0));
                i++;
            } while (cursor.moveToNext());
        }
        return id;
    }


    public String[] displayTodo() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] todo = new String[rowCount];
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

//get radius
    public int displayRadius(int i) {
        String selectQuery = "SELECT "+COLUMN_NAME_RADIUS +" FROM " + TABLE_NAME +" WHERE " + COLUMN_NAME_ID + " = " +i;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int rad = 0;
        if (cursor.moveToFirst()) {
            rad = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_RADIUS));
        }
        cursor.close();

        return rad;
    }


//get individual result to manage task
    public String[] displayTask(int i) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME +" WHERE " +COLUMN_NAME_ID +" = " +i;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] myTask = new String[10];
        if (cursor.moveToFirst()) {
            myTask[0] = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK));
            myTask[1] = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE));
            myTask[2] = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TODO));
            myTask[3] = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
        }
        cursor.close();
        return myTask;
    }


    //To delete a Task entry
    public void delTask(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_ID + " = " + i);
    }


    //To update the databse
    public void updateTask(int id, String task, String todo, Integer radius, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME + " SET "
                +COLUMN_NAME_TASK + " = '" + task +"', "
                +COLUMN_NAME_TODO + " = '" + todo +"', "
                +COLUMN_NAME_RADIUS + " = " + radius +", "
                +COLUMN_NAME_DATE + " = '" + date +"' "
                +" WHERE " +COLUMN_NAME_ID + " = " + id + ";");
    }


    //To delete a Task entry
    public void getCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        rowCount = (int)numRows;
    }

}

