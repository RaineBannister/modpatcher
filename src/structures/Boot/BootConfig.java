package structures.Boot;

import structures.Boot.ConfigTypes.*;
import structures.Boot.ConfigTypes.Boolean;
import structures.Boot.ConfigTypes.Integer;
import structures.Boot.ConfigTypes.String;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BootConfig {
    private HashMap<java.lang.String, Type> config;

    public BootConfig() {
        config = new HashMap<>();
    }

    public void addConfig(java.lang.String key, java.lang.String data) {
        this.config.put(key, new String(data));
    }

    public void addConfig(java.lang.String key, int data) {
        this.config.put(key, new Integer(data));
    }

    public void addConfig(java.lang.String key, boolean data) {
        this.config.put(key, new Boolean(data));
    }

    public void addUnsignedIntConfig(java.lang.String key, int data) {
        this.config.put(key, new UnsignedInteger(data));
    }

    public void toFile(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(this.toString());
    }

    public java.lang.String toString() {
        StringBuilder toRet = new StringBuilder();
        Iterator it = this.config.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            toRet.append(pair.getKey()).append("=").append(pair.getValue().toString());

            if(it.hasNext()) toRet.append(",\n");

            it.remove(); // avoids a ConcurrentModificationException
        }
        return toRet.toString();
    }
}
