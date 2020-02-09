package cdclient;

import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class RowInfo {
    private RowInfo linked;
    private RowData rowData;

    public RowInfo(BinaryStream stream) throws IOException {
        stream.follow();
        rowData = new RowData(stream);
        stream.back();
        int next = stream.readIntLittleEndian();
        if(next != -1) {
            stream.go(next);
            linked = new RowInfo(stream);
            stream.back();
        } else {
            linked = null;
        }
    }

    public RowInfo(JSONObject object) {
        if(object.isNull("linked")) {
            linked = null;
        } else {
            linked = new RowInfo(object.getJSONObject("linked"));
        }

        rowData = new RowData(object.getJSONObject("rowData"));
    }

    public JSONObject toJSON(JSONObject json) {
        if(linked != null) {
            json.put("linked", linked.toJSON(new JSONObject()));
        } else {
            json.put("linked", (Object) null);
        }

        json.put("rowData", rowData.toJSON(new JSONObject()));
        return json;
    }

    public void serialize(BinaryStream stream) throws IOException{
        long pointer = stream.getFilePointer();
        stream.writeIntLittleEndian(0);
        stream.go(pointer);
        stream.writeIntLittleEndian(stream.length());
        stream.go(stream.length());
        this.rowData.serialize(stream);

        if(this.linked == null) stream.writeIntLittleEndian(-1);
        else this.linked.serialize(stream);

        stream.back();
    }
}
