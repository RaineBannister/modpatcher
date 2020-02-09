package cdclient;

import main.BinaryStream;
import cdclient.data.Data;
import cdclient.data.DataFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class RowData {
    private Data[] data;

    public RowData(BinaryStream stream) throws IOException {
        data = new Data[stream.readIntLittleEndian()];
        stream.follow();
        for(int i = 0; i < data.length; i ++) {
            int type = stream.readIntLittleEndian();
            data[i] = DataFactory.newData(DataTypes.values()[type], stream);
        }
        stream.back();
    }

    public RowData(JSONObject object) {
        JSONArray array = object.getJSONArray("data");
        data = new Data[array.length()];
        for(int i = 0; i < array.length(); i ++) {
            if(array.isNull(i)) {
                data[i] = null;
            } else {
                data[i] = DataFactory.newData(array.getJSONObject(i));
            }
        }
    }

    public JSONObject toJSON(JSONObject json) {
        JSONArray array = new JSONArray();

        for(int i = 0; i < data.length; i ++) {
            if(data[i] == null) array.put((Object) null);
            else array.put(data[i].toJSON(new JSONObject()));
        }

        json.put("data", array);
        return json;
    }

    public void serialize(BinaryStream stream) throws IOException {
        stream.writeIntLittleEndian(this.data.length);
    }
}
