package com.webrdaniel.collectmydata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;


class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME ="dbCollectMyData";
    private static final String RECORDS_TABLE ="tblRecords";
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

        String queryValuesTable = "CREATE TABLE "+ RECORDS_TABLE +" ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "_idDataColl INTEGER, "+
                "value DOUBLE, "+
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

    public int insertDataValue(int IDDataColl, double value, String date )
    {
        ContentValues dataCollValues = new ContentValues();
        dataCollValues.put("_idDataColl", IDDataColl);
        dataCollValues.put("value", value);
        dataCollValues.put("date", date);
        return (int) this.getWritableDatabase().insert(RECORDS_TABLE,null, dataCollValues);
    }

    public void deleteDataSQLite( ){
        this.getWritableDatabase().delete(DATA_COLL_TABLE,null,null );
        this.getWritableDatabase().delete(RECORDS_TABLE,null,null );

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
                      cursor.getInt(0),
                      cursor.getString(1),
                      cursor.getInt(2),
                      cursor.getString(3));
              dataCollItems.add(item);
          }while(cursor.moveToNext());
      }
        cursor.close();
        return dataCollItems;
    }

    public ArrayList<Record> getRecords(int dataCollId)
    {
        String query = "SELECT  * FROM " + RECORDS_TABLE + " WHERE _idDataColl=" + dataCollId;
        ArrayList<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do{
                records.add( new Record(
                        cursor.getInt(0),
                        Utils.stringToDate(cursor.getString(3),Utils.DATE_FORMAT_DMY),
                        cursor.getDouble(2)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(records, new RecordComparator());
        return records;
    }

    public double getValuesSelect (String type, int dataCollId)
    {
        double result;
        String query;
        switch (type)
        {
            case "COUNT": query = "SELECT COUNT(_id) FROM "+ RECORDS_TABLE + " WHERE _idDataColl="+ dataCollId;break;
            case "MIN": query = "SELECT MIN(value) FROM "+ RECORDS_TABLE + " WHERE _idDataColl="+ dataCollId;break;
            case "MAX": query = "SELECT MAX(value) FROM "+ RECORDS_TABLE + " WHERE _idDataColl="+ dataCollId;break;
            case "AVG": query = "SELECT AVG(value) FROM "+ RECORDS_TABLE + " WHERE _idDataColl="+ dataCollId;break;
            case "SUM": query = "SELECT SUM(value) FROM "+ RECORDS_TABLE + " WHERE _idDataColl="+ dataCollId;break;
            default: return 0;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            result = cursor.getDouble(0);
        }
        else
        {
            result = 0;
        }
        cursor.close();
        return result;
    }

    public void renameDataColl(int id, String name) {
        String queryRenameDataColl = "UPDATE "+DATA_COLL_TABLE+
                " SET name = '"+ name +"'"+
                " WHERE _id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryRenameDataColl);
    }

    public void deleteDataColl(int id) {
        String queryDeleteDataColl = "DELETE FROM "+DATA_COLL_TABLE+
                " WHERE _id = " + id;
        String queryDeleteValues = "DELETE FROM "+ RECORDS_TABLE +
                " WHERE _idDataColl = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryDeleteDataColl);
        db.execSQL(queryDeleteValues);
    }

    public void editValue(int id, double value) {
        String queryRenameDataColl = "UPDATE "+ RECORDS_TABLE +
                " SET value = '"+ value +"'"+
                " WHERE _id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryRenameDataColl);
    }

    public void deleteRecord(int id) {
        String queryDeleteValues = "DELETE FROM "+ RECORDS_TABLE +
                " WHERE _id= " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryDeleteValues);
    }
}
