package cdclient.data;

import main.BinaryStream;
import cdclient.DataTypes;
import org.json.JSONObject;

import java.io.IOException;

public class DataFactory {
    public static Data newData(DataTypes type, BinaryStream stream) throws IOException {
        Data ret = null;
        switch (type) {
            case NOTHING:
                break;
            case INTEGER:
                ret =  new Integer(stream);
                break;
            case UNKNOWN1:
                break;
            case FLOAT:
                ret = new Float(stream);
                break;
            case TEXT:
                ret = new Text(stream);
                break;
            case BOOLEAN:
                ret = new Boolean(stream);
                break;
            case BIGINT:
                ret = new BigInt(stream);
                break;
            case UNKNOWN2:
                break;
            case VARCHAR:
                ret = new VarChar(stream);
                break;
        }
        return ret;
    }

    public static Data newData(JSONObject object) {
        DataTypes type = DataTypes.values()[object.getInt("type")];
        Data newDataObject = null;
        switch (type) {
            case NOTHING:
                break;
            case INTEGER:
                newDataObject =  new Integer(object);
                break;
            case UNKNOWN1:
                break;
            case FLOAT:
                newDataObject = new Float(object);
                break;
            case TEXT:
                newDataObject = new Text(object);
                break;
            case BOOLEAN:
                newDataObject = new Boolean(object);
                break;
            case BIGINT:
                newDataObject = new BigInt(object);
                break;
            case UNKNOWN2:
                break;
            case VARCHAR:
                newDataObject = new VarChar(object);
                break;
        }
        return newDataObject;
    }
}
