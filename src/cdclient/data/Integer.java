package cdclient.data;

import cdclient.DataTypes;
import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class Integer extends Data {
    private int data;
    public Integer(BinaryStream stream) throws IOException {
        this.data = stream.readIntLittleEndian();
    }

    public Integer(JSONObject object) {
        this.data = object.getInt("data");
    }

    public DataTypes getType() {
        return DataTypes.INTEGER;
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("type", DataTypes.INTEGER.ordinal());
        json.put("data", data);
        return json;
    }
}
