package com.webrdaniel.collectmydata.models;

import java.io.Serializable;

public class mDataCollItem implements Serializable {
    private String name;
    private int color;
    private int id;

    public mDataCollItem(){

    }
    public mDataCollItem(int id, String name, int color){
        this.id = id;
        this.name = name;
        this.color = color;
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
