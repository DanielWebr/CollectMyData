package com.webrdaniel.collectmydata;

import java.util.Date;

public class Record{
    private int id;
    private Date date;
    private double value;

    Record(int id, Date date, double value) {
        this.id = id;
        this.date = date;
        this.value = value;
    }

    Record(Date date, double value) {
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
