package com.webrdaniel.collectmydata.utils;

import android.media.MediaScannerConnection;
import android.os.Environment;
import android.widget.Toast;

import com.webrdaniel.collectmydata.R;
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
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
            if (!file.exists()) file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true /*append*/));

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
                    new String[]{file.toString()},
                    null,
                    null);

            Toast.makeText(context, context.getString(R.string.CSV_saved), Toast.LENGTH_SHORT).show();
    }
}
