package com.webrdaniel.collectmydata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;


class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME ="dbCollectMyData";
    private static final String VALUES_TABLE ="tblValues";
    private static final String DATA_COLL_TABLE ="tblDataColl";
    private static final int DB_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context,DB_NAME,null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryDataCollTable = "CREATE TABLE "+DATA_COLL_TABLE+" ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "name TEXT, "+
                "color INTEGER, "+
                "reminderTime TEXT);";

        String queryValuesTable = "CREATE TABLE "+VALUES_TABLE+" ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "_idDataColl INTEGER, "+
                "value INTEGER, "+
                "date TEXT);";
        db.execSQL(queryDataCollTable);
        db.execSQL(queryValuesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insertDataColl( String name, int color, String reminderTime )
    {
        ContentValues dataCollValues = new ContentValues();
        dataCollValues.put("name", name);
        dataCollValues.put("color", color);
        dataCollValues.put("reminderTime", reminderTime);
        return (int) this.getWritableDatabase().insert(DATA_COLL_TABLE,null, dataCollValues);
    }

    public void insertDataValue(int IDDataColl, int value, String date )
    {
        ContentValues dataCollValues = new ContentValues();
        dataCollValues.put("_idDataColl", IDDataColl);
        dataCollValues.put("value", value);
        dataCollValues.put("date", date);
         this.getWritableDatabase().insert(VALUES_TABLE,null, dataCollValues);
    }

    public void deleteDataSQLite( ){
        this.getWritableDatabase().delete(DATA_COLL_TABLE,null,null );
        this.getWritableDatabase().delete(VALUES_TABLE,null,null );

    }

    public ArrayList<DataCollItem> getDataCollItems()
    {
        String query = "SELECT  * FROM " + DATA_COLL_TABLE;
        ArrayList<DataCollItem> dataCollItems = new  ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
      if(cursor.moveToFirst())
      {
          do{
              DataCollItem item = new DataCollItem(
                      Integer.parseInt(cursor.getString(0)),
                      cursor.getString(1),
                      Integer.parseInt(cursor.getString(2)),
                      cursor.getString(3));
              dataCollItems.add(item);
          }while(cursor.moveToNext());
      }
        cursor.close();
        return dataCollItems;
    }

    public HashMap<Date,Integer> getValues (int dataCollId)
    {
        String query = "SELECT  * FROM " + VALUES_TABLE + " WHERE _idDataColl="+ dataCollId;
        HashMap<Date,Integer> values = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do{
                values.put(
                        Utils.stringToDate(cursor.getString(3),"ss:mm:HH d.M.yyyy"),
                        Integer.parseInt(cursor.getString(2)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return values;
    }

}
