package com.webrdaniel.collectmydata;

import java.io.Serializable;

class DataCollItem implements Serializable {
    private String name;
    private int color;
    private int id;

    DataCollItem(){

    }
    DataCollItem(int id, String name, int color){
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
