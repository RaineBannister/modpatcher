package cdclient;

import main.BinaryStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class RowTopHeader {
    private RowInfo[] rowInfos;

    public RowTopHeader(BinaryStream stream) throws IOException {
        stream.follow();
        rowInfos = new RowInfo[stream.readIntLittleEndian()];
        int next = stream.readIntLittleEndian();
        if(next != -1) {
            stream.go(next);
            for(int i = 0; i < rowInfos.length; i ++) {
                next = stream.readIntLittleEndian();
                if(next != -1) {
                    stream.go(next);
                    rowInfos[i] = new RowInfo(stream);
                    stream.back();
                } else {
                    rowInfos[i] = null;
                }
            }
            stream.back();
        }
        stream.back();
    }

    public RowTopHeader(JSONObject object) {
        JSONArray array = object.getJSONArray("rowInfos");
        this.rowInfos = new RowInfo[array.length()];
        for(int i = 0; i < array.length(); i ++) {
            if(array.isNull(i)) this.rowInfos[i] = null;
            else this.rowInfos[i] = new RowInfo(array.getJSONObject(i));
        }
    }

    public JSONObject toJSON(JSONObject json) {
        JSONArray array = new JSONArray();
        for(int i = 0; i < rowInfos.length; i ++) {
            if(rowInfos[i] != null) {
                array.put(rowInfos[i].toJSON(new JSONObject()));
            } else {
                array.put((Object) null);
            }
        }
        json.put("rowInfos", array);
        return json;
    }

    public void serialize(BinaryStream stream) throws IOException {
        stream.writeIntLittleEndian(this.rowInfos.length);
        stream.writeIntLittleEndian(stream.length());

        stream.go(stream.length());
        long blockStart = stream.getFilePointer();
        for(int i = 0; i < this.rowInfos.length; i ++) {
            stream.writeInt(0);
        }

        stream.seek(blockStart);

        for(int i = 0; i < this.rowInfos.length; i ++) {
            if(this.rowInfos[i] != null)
                this.rowInfos[i].serialize(stream);
            else
                stream.writeIntLittleEndian(-1);
        }
    }
}
