package com.webrdaniel.collectmydata;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

 class DataCollItem implements Serializable {
    private String mDataCollName;
    private UUID mTodoIdentifier;
    private int mColor;
    private static final String DATACOLLITEM = "datacollitem";
     private static final String COLOR = "todocolor";

    public DataCollItem(){

    }


    public DataCollItem(JSONObject jsonObject) throws JSONException {
        mDataCollName = jsonObject.getString(DATACOLLITEM);
        mColor = jsonObject.getInt(COLOR);

    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DATACOLLITEM, mDataCollName);
        jsonObject.put(COLOR, mColor);
        return jsonObject;
    }

    public void setmDataCollName(String mDataCollName) {
        this.mDataCollName = mDataCollName;
    }

    public String getmDataCollName() {
        return mDataCollName;
    }
     public UUID getIdentifier(){
         return mTodoIdentifier;
     }

     public void setmColor(int mTodoColor) {
         this.mColor = mTodoColor;
     }

     public int getmColor() {
         return mColor;
     }
}
