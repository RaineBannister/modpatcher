package cdclient.data;

import cdclient.DataTypes;
import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class BigInt extends Data {
    private long data;
    public BigInt(BinaryStream stream) throws IOException {
        stream.follow();
        this.data = stream.readLongLittleEndian();
        stream.back();
    }

    public BigInt(JSONObject object) {
        this.data = object.getLong("data");
    }

    public DataTypes getType() {
        return DataTypes.BIGINT;
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("type", DataTypes.BIGINT.ordinal());
        json.put("data", data);
        return json;
    }
}
