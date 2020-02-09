package cdclient;

import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class Table {
    private ColumnHeader columnHeader;
    private RowTopHeader rowTopHeader;
    private long pointer;

    public Table(BinaryStream stream) throws IOException {
        this.pointer = stream.getFilePointer();
        this.columnHeader = new ColumnHeader(stream);
        this.rowTopHeader = new RowTopHeader(stream);
    }

    public Table(BinaryStream stream, boolean loadRowData) throws IOException {
        this.pointer = stream.getFilePointer();
        this.columnHeader = new ColumnHeader(stream);
        if(loadRowData) this.rowTopHeader = new RowTopHeader(stream);
        else stream.readIntLittleEndian();
    }

    public Table(JSONObject object) {
        this.columnHeader = new ColumnHeader(object.getJSONObject("columnHeader"));
        this.rowTopHeader = new RowTopHeader(object.getJSONObject("rowTopHeader"));
    }

    public String getName() {
        return this.columnHeader.getName();
    }

    public String[] getColumnNames() {
        return this.columnHeader.listColumnNames();
    }

    public void loadRowTopHeader(BinaryStream stream) throws IOException {
        stream.go(this.pointer + 4);
        this.rowTopHeader = new RowTopHeader(stream);
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("columnHeader", this.columnHeader.toJSON(new JSONObject()));
        json.put("rowTopHeader", this.rowTopHeader.toJSON(new JSONObject()));
        return json;
    }

    public void serialize(BinaryStream stream) throws IOException {
        stream.writeIntLittleEndian((int) stream.length());
        stream.go(stream.length());
        this.columnHeader.serialize(stream);
        stream.back();

        stream.writeIntLittleEndian((int) stream.length());
        stream.go(stream.length());
        this.rowTopHeader.serialize(stream);
        stream.back();
    }
}
