package com.webrdaniel.collectmydata.models;

import java.util.Date;

public class Record{
    private int id;
    private Date date;
    private double value;

    public Record(int id, Date date, double value) {
        this.id = id;
        this.date = date;
        this.value = value;
    }

    public Record(Date date, double value) {
        this.date = date;
        this.value = value;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }


}
