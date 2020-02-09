package cdclient;

import main.BinaryStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ColumnHeader {

    private String name;

    private Column[] columns;

    public ColumnHeader(BinaryStream stream) throws IOException{
        //Column Data...
        stream.follow();
        int numberOfColumns = stream.readIntLittleEndian();
        this.name = stream.readString();
        stream.follow();
        this.columns = new Column[numberOfColumns];
        for(int i = 0; i < this.columns.length; i ++) {
            this.columns[i] = new Column(stream);
        }
        stream.back();
        stream.back();
    }

    public ColumnHeader(JSONObject object) {
        this.name = object.getString("name");
        JSONArray array = object.getJSONArray("columns");
        this.columns = new Column[array.length()];
        for(int i = 0; i < array.length(); i ++) {
            this.columns[i] = new Column(array.getJSONObject(i));
        }
    }

    public String[] listColumnNames() {
        String[] names = new String[this.columns.length];
        for(int i = 0; i < this.columns.length; i ++) {
            names[i] = this.columns[i].getName();
        }

        return names;
    }

    public String getName() {
        return this.name;
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("name", this.name);
        JSONArray array = new JSONArray();
        for(int i = 0; i < this.columns.length; i ++) {
            array.put(this.columns[i].toJSON(new JSONObject()));
        }
        json.put("columns", array);
        return json;
    }

    public void serialize(BinaryStream stream) throws IOException {
        stream.writeIntLittleEndian(columns.length);
        long tableNamePointer = stream.getFilePointer();
        stream.writeInt(0);

        long pointerToColumns = stream.getFilePointer();
        stream.writeInt(0);

        long columnsBlock = stream.getFilePointer();

        stream.go(pointerToColumns);
        stream.writeIntLittleEndian(columnsBlock);
        stream.back();

        for(int i = 0; i < columns.length; i ++) {
            stream.writeInt(0);
            stream.writeInt(0);
        }

        stream.go(tableNamePointer);
        stream.writeString(this.name);
        stream.back();

        stream.seek(columnsBlock);
        for(int i = 0; i < columns.length; i ++) {
            this.columns[i].serialize(stream);
        }
    }
}
