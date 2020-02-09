package cdclient;

import main.BinaryStream;
import org.json.JSONObject;

import java.io.IOException;

public class Column {
    private String name;
    private int type;

    public Column(BinaryStream stream) throws IOException{
        this.type = stream.readIntLittleEndian();
        this.name = stream.readString();
    }

    public Column(JSONObject object) {
        this.type = object.getInt("type");
        this.name = object.getString("name");
    }

    public String getName() {
        return this.name;
    }

    public JSONObject toJSON(JSONObject json) {
        json.put("type", this.type);
        json.put("name", this.name);
        return json;
    }

    public void serialize(BinaryStream stream) throws IOException {
        stream.writeIntLittleEndian(this.type);
        stream.writeString(this.name);
    }
}
