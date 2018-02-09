package com.webrdaniel.collectmydata.Models;

import java.util.Comparator;

public class RecordComparator implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        int x = o1.getDate().compareTo(o2.getDate());
        return (x == 0) ? 0 : (x==1) ? -1 : 1;
    }
}
