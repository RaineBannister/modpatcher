package cdclient.data;

import cdclient.DataTypes;
import org.json.JSONObject;

public abstract class Data {
    public abstract DataTypes getType();
    public abstract JSONObject toJSON(JSONObject json);
}
