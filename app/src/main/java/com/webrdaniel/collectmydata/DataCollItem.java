package com.webrdaniel.collectmydata;


import android.util.Log;

import java.io.Serializable;
import java.util.Date;

class DataCollItem implements Serializable {
    private String name;
    private int color;
    private Date reminderTime;
    private int id;

    public DataCollItem(){
        reminderTime = Utils.stringToDate("11:25",Utils.DATE_FORMAT_REMINDER);
    }
    public DataCollItem(int id, String name, int color, String reminderTime){
        this.id = id;
        this.name = name;
        this.color = color;
        this.reminderTime = Utils.stringToDate(reminderTime,Utils.DATE_FORMAT_REMINDER);
    }

    public Date getReminderTime() {
        return reminderTime;
    }
    public String getReminderTimeString() {
        return Utils.dateToString(reminderTime,Utils.DATE_FORMAT_REMINDER);
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
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
