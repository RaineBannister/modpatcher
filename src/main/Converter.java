package main;

import cdclient.DB;
import cdclient.Table;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Converter {
    public static void convertToJson(String inputFilepath) {
        convertToJson(inputFilepath, "./");
    }

    public static void convertToJson(String inputFilepath, String outputFilepath) {
        try{
            DB cdclient = new DB(inputFilepath);

            String[] names = cdclient.getTableNames();
            List<String> nameList = Arrays.asList(names);

            JSONArray index = new JSONArray();
            for(int i = 0; i < names.length; i ++) {
                index.put(names[i] + ".json");
            }

            BufferedWriter indexWriter = new BufferedWriter(new FileWriter(outputFilepath + "/cdclient.json"));
            indexWriter.write(index.toString(4));
            indexWriter.close();

            AtomicInteger count = new AtomicInteger(0);
            System.out.println("Converting cdclient to JSON...");
            nameList.forEach(name -> {
                try {
                    BinaryStream threadStream = new BinaryStream(cdclient.getStream());

                    System.out.print("\r" + bar((count.get()+1)*1.0/names.length) + "[" + String.format("%.2f", ((count.get()+1)*1.0/names.length) * 100) + "%]" + " [" + name + "]");
                    cdclient.loadTableData(name, threadStream);

                    // write JSON to files...
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilepath + "/" + name + ".json"));
                    writer.write(cdclient.getTableByName(name).toJSON(new JSONObject()).toString(4));
                    writer.close();

                    // free up memory
                    cdclient.unloadTableData(name);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace(System.out);
                }

                count.getAndIncrement();
            });

            System.out.print("\r" + bar((count.get()+1)*1.0/names.length) + "[" + String.format("%.2f", ((count.get()+1)*1.0/names.length) * 100) + "%]");
            System.out.println("\nDone!");


        } catch(IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static void convertFromJson(String inputJsonIndexDir) {
        convertFromJson(inputJsonIndexDir, "cdclient.fdb");
    }

    public static void convertFromJson(String inputJsonIndexDir, String outFileName) {
        try {
            File indexJson = new File(inputJsonIndexDir + "/cdclient.json");
            Scanner scanner = new Scanner(indexJson);
            scanner.useDelimiter("\\Z");

            String indexJsonString = scanner.next();

            JSONArray index = new JSONArray(indexJsonString);
            DB cdclient = new DB(index.length());

            for (int i = 0; i < index.length(); i ++) {
                File tableJson = new File(inputJsonIndexDir + "/" + index.getString(i));
                Scanner tableScanner = new Scanner(tableJson);
                tableScanner.useDelimiter("\\Z");
                Table table = new Table(new JSONObject(tableScanner.next()));
                System.out.println(table.getName());
                cdclient.addTable(table);
            }

            cdclient.serialize("cdclient_test.fdb");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static String bar(double percentage) {
        String bar = "[";
        int length = 50;
        for(int i = 0; i < length; i ++) {
            if(percentage*length < i) bar += "░";
            else bar += "█";
        }
        return bar + "]";
    }
}
