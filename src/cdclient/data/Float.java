package cdclient.data;

import cdclient.DataTypes;
import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class Float extends Data{
    private float data;
    public Float(BinaryStream stream) throws IOException {
        this.data = stream.readFloat(); // TODO: Needs to be in little endian... grr
    }

    public Float(JSONObject object) {
        this.data = object.getFloat("data");
    }

    public DataTypes getType() {
        return DataTypes.FLOAT;
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("type", DataTypes.FLOAT.ordinal());
        json.put("data", data);
        return json;
    }
}
