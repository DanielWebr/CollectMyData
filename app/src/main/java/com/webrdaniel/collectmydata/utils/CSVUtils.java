package com.webrdaniel.collectmydata.utils;

import android.os.Environment;

import com.webrdaniel.collectmydata.activities.DataCollDetailActivity;
import com.webrdaniel.collectmydata.models.Record;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ';';


    public static void recordsToCSV(DataCollDetailActivity context, ArrayList<Record> records) throws IOException {

        String name = context.getDataCollItem().getName()+".csv";
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File file = new File(dir.getAbsolutePath()+"/"+name);

        file.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true ));

        StringBuilder content = new StringBuilder();
        for (Record record : records) {
            content.append(DateUtils.dateToString(record.getDate(),DateUtils.DATE_FORMAT_DMY));
            content.append(DEFAULT_SEPARATOR);
            content.append(Utils.doubleToString(record.getValue()));
            content.append("\n");
        }

        writer.write(content.toString());

        writer.close();


    }
}
