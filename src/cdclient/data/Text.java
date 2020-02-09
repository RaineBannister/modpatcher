package cdclient.data;

import cdclient.DataTypes;
import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class Text extends Data {
    private String data;
    public Text(BinaryStream stream) throws IOException {
        this.data = stream.readString();
    }

    public Text(JSONObject object) {
        this.data = object.getString("data");
    }

    public DataTypes getType() {
        return DataTypes.TEXT;
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("type", DataTypes.TEXT.ordinal());
        json.put("data", data);
        return json;
    }
}
