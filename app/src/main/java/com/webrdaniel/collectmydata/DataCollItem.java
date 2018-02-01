package com.webrdaniel.collectmydata;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;

class DataCollItem implements Serializable {
    private String name;
    private int color;
    private Time reminderTime;
    private static final String DATACOLLITEM = "datacollitem";
    private static final String COLOR = "todocolor";

    public DataCollItem(){
        reminderTime = new Time(125);
    }

    public DataCollItem(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString(DATACOLLITEM);
        color = jsonObject.getInt(COLOR);

    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DATACOLLITEM, name);
        jsonObject.put(COLOR, color);
        return jsonObject;
    }
    public Time getReminderTime() {
        return reminderTime;
    }
    public void setReminderTime(Time reminderTime) {
        this.reminderTime = reminderTime;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setColor(int mTodoColor) {
        this.color = mTodoColor;
    }
    public int getColor() {
        return color;
    }
}
