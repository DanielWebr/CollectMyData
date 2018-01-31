package com.webrdaniel.collectmydata;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

 class DataCollItem implements Serializable {
    private String mDataCollName;
    private UUID mTodoIdentifier;
    private static final String DATACOLLITEM = "datacollitem";

    public DataCollItem(){
        mTodoIdentifier = UUID.randomUUID();
    }


    public DataCollItem(JSONObject jsonObject) throws JSONException {
        mDataCollName = jsonObject.getString(DATACOLLITEM);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DATACOLLITEM, mDataCollName);
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
}
