package com.webrdaniel.collectmydata.utils;

import android.media.MediaScannerConnection;

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
            File testFile = new File(context.getExternalFilesDir(null), name);
            if (!testFile.exists()) testFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));

            StringBuilder content = new StringBuilder();
            for (Record record : records) {
                content.append(DateUtils.dateToString(record.getDate(),DateUtils.DATE_FORMAT_DMY));
                content.append(DEFAULT_SEPARATOR);
                content.append(Utils.doubleToString(record.getValue()));
                content.append("\n");
            }

            writer.write(content.toString());

            writer.close();

            MediaScannerConnection.scanFile(context,
                    new String[]{testFile.toString()},
                    null,
                    null);


    }
}
