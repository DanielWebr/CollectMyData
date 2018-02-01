package com.webrdaniel.collectmydata;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.sql.Time;


class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME ="CollectMyData";
    private static final int DB_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context,DB_NAME,null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryDataCollTable = "CREATE TABLE DATA_COLL_TABLE ("+
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "NAME TEXT, "+
                "COLOR INTEGER, "+
                "REMINDER_TIME INTEGER);";

        String queryDataValueTable = "CREATE TABLE DATA_VALUE_TABLE ("+
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "ID_DATA_COLL INTEGER, "+
                "VALUE INTEGER, "+
                "DATE INTEGER);";
        db.execSQL(queryDataCollTable);
        db.execSQL(queryDataValueTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
