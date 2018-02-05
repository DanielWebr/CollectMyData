package com.webrdaniel.collectmydata;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by dandu on 04.02.2018.
 */

public class Record{
    int id;
    Date date;
    double value;

    public Record (int id,Date date, double value)
    {
        this.id = id;
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
