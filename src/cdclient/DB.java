package cdclient;

import main.BinaryStream;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;

public class DB {
    private HashMap<String, Table> tables;
    private BinaryStream stream;

    public DB(int numberOfTables) {
        this.tables = new HashMap<String, Table>(numberOfTables);
    }

    public DB(String filename) throws IOException{
        this(new File(filename));
    }

    public DB(File file) throws IOException{
        this(new BinaryStream(file, "r"));
    }

    public DB(BinaryStream stream) throws IOException{
        this.stream = stream;
        int size = this.stream.readIntLittleEndian();
        this.tables = new HashMap<String, Table>(size);
        this.stream.follow(); //Go to header section...
        for(int i = 0; i < size; i ++) {
            Table temp = new Table(this.stream, false);
            this.tables.put(temp.getName(), temp);
        }
    }

    public String[] getTableNames() {
        String[] names = new String[this.tables.size()];
        for(int i = 0; i < this.tables.size(); i ++) {
            names[i] = this.tables.values().toArray(new Table[0])[i].getName();
        }
        return names;
    }

    public Table getTableByName(String name) {
        return this.tables.get(name);
    }

    public void loadTableData(String name) throws IOException {
        this.loadTableData(name, this.stream);
    }

    public void loadTableData(String name, BinaryStream stream) throws IOException {
        this.getTableByName(name).loadRowTopHeader(stream);
    }

    public void unloadTableData(String name) {
        this.tables.remove(name);
    }

    public JSONObject toJSON(JSONObject json) {
        return json;
    }

    public BinaryStream getStream() {
        return this.stream;
    }

    public void addTable(Table table) {
        this.tables.put(table.getName(), table);
    }

    public void serialize(String filename) throws IOException {
        this.serialize(new File(filename));
    }

    public void serialize(File file) throws IOException {
        this.serialize(new BinaryStream(file, "rw"));
    }

    public void serialize(BinaryStream stream) throws IOException {
        this.stream = stream;
        this.stream.writeIntLittleEndian(this.tables.size());

        long pointerToLock = this.stream.getFilePointer();
        this.stream.writeIntLittleEndian(0);

        long blockStart = this.stream.getFilePointer();

        stream.go(pointerToLock);
        stream.writeIntLittleEndian((int) blockStart);
        stream.back();

        //allocate a block for pointers
        for(int i = 0; i < this.tables.size(); i++) {
            this.stream.writeInt(0);
            this.stream.writeInt(0);
        }

        stream.seek(blockStart);

        for(int i = 0; i < this.tables.size(); i++) {
            this.tables.values().toArray(new Table[0])[i].serialize(this.stream);
        }
    }
}
