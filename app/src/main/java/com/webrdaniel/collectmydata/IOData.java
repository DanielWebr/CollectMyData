package com.webrdaniel.collectmydata;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class IOData {
    private Context mContext;
    private String mFileName;

    public IOData(Context context, String filename){
        mContext = context;
        mFileName = filename;
    }

    private static JSONArray toJSONArray(ArrayList<DataCollItem> items) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(DataCollItem item : items){
            JSONObject jsonObject = item.toJSON();
            jsonArray.put(jsonObject);
        }
        return  jsonArray;
    }

    public void saveToFile(ArrayList<DataCollItem> items) throws JSONException, IOException {
        FileOutputStream fileOutputStream;
        OutputStreamWriter outputStreamWriter;
        fileOutputStream = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(toJSONArray(items).toString());
        outputStreamWriter.close();
        fileOutputStream.close();
    }
    public void deleteData(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(mFileName);
            writer.print("");
        } catch (FileNotFoundException e) {

        }
        finally {
            if(writer!=null)
            {
                writer.close();
            }
        }
    }

    private ArrayList<DataCollItem> loadFromFile() throws IOException, JSONException{
        ArrayList<DataCollItem> items = new ArrayList<>();
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream =  mContext.openFileInput(mFileName);
            StringBuilder builder = new StringBuilder();
            String line;
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while((line = bufferedReader.readLine())!=null){
                builder.append(line);
            }

            JSONArray jsonArray = (JSONArray)new JSONTokener(builder.toString()).nextValue();
            for(int i =0; i<jsonArray.length();i++){
                DataCollItem item = new DataCollItem(jsonArray.getJSONObject(i));
                items.add(item);
            }


        } catch (FileNotFoundException fnfe) {
                //first run
        }
        finally {
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(fileInputStream!=null){
                fileInputStream.close();
            }

        }
        return items;
    }

    public ArrayList<DataCollItem> getLocallyStoredData(){
        ArrayList<DataCollItem> items = null;
        try {
            items  = loadFromFile();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        if(items == null){
            items = new ArrayList<>();
        }
        return items;
    }

    public static void deleteDataSQLite(DatabaseHelper dbHelper){
        dbHelper.getReadableDatabase().delete("DATA_COLL_TABLE",null,null );
    }

    public static void insertDataColl(DatabaseHelper dbHelper, String name, int color, Time reminderTime )
    {
        ContentValues dataCollValues = new ContentValues();
        dataCollValues.put("NAME", name);
        dataCollValues.put("COLOR", color);
        dataCollValues.put("REMINDER_TIME", reminderTime.toString());
        dbHelper.getReadableDatabase().insert("DATA_COLL_TABLE",null, dataCollValues);
    }

    public static void insertDataValue(SQLiteDatabase db, int IDDataColl, int value, Date date )
    {
        ContentValues dataCollValues = new ContentValues();
        dataCollValues.put("ID_DATA_COLL", IDDataColl);
        dataCollValues.put("VALUE", value);
        dataCollValues.put("DATE", date.toString());
        db.insert("DATA_VALUE_TABLE",null, dataCollValues);
    }
}
