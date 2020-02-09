package cdclient.data;

import cdclient.DataTypes;
import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class Boolean extends Data {
    private boolean data;
    public Boolean(BinaryStream stream) throws IOException {
        this.data = stream.readIntLittleEndian() == 1;
    }

    public Boolean(JSONObject object) {
        this.data = object.getBoolean("data");
    }

    public DataTypes getType() {
        return DataTypes.BOOLEAN;
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("type", DataTypes.BOOLEAN.ordinal());
        json.put("data", data);
        return json;
    }
}
