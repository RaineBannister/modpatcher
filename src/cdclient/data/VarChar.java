package cdclient.data;

import cdclient.DataTypes;
import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class VarChar extends Data {
    private String data;
    public VarChar(BinaryStream stream) throws IOException {
        this.data = stream.readString();
    }

    public VarChar(JSONObject object) {
        this.data = object.getString("data");
    }

    public DataTypes getType() {
        return DataTypes.VARCHAR;
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("type", DataTypes.VARCHAR.ordinal());
        json.put("data", data);
        return json;
    }
}
