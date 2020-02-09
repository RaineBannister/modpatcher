import main.Converter;
import main.GUI;

import java.util.HashMap;

enum Role {
    CONVERT_FDB_TO_JSON,
    CONVERT_JSON_TO_FDB,
    NORMAL
}

enum LogLevel {
    NONE,
    ALL
}

public class Main {
    public static void main(String[] args) {

        // set up defaults
        boolean console = false;
        boolean debug = false;
        Role role = Role.NORMAL;
        LogLevel logLevel = LogLevel.NONE;

        // load up the config options from command line
        HashMap<String, String> config = new HashMap<>();
        for(int i = 0; i < args.length; i ++) {
            String arg = args[i];
            if(arg.charAt(0) == '-') {
                i++;
                String val = "";
                // check to see if we hit the end of string and make sure we also don't hit the next flag
                if(i < args.length && args[i].charAt(0) != '-') {
                    val = args[i];
                } else if (i < args.length && args[i].charAt(0) == '-') { // if we do hit the next flag, get the next iteration ready
                    i--;
                }
                config.put(arg.substring(1), val);
            }
        }

        // set flags
        if(config.containsKey("console")) {
            console = true;
        }

        if(config.containsKey("debug")) {
            debug = true;
        }

        if(config.containsKey("convert-to-json")) {
            role = Role.CONVERT_FDB_TO_JSON;
            console = true;
        }

        if(config.containsKey("convert-to-fdb")) {
            role = Role.CONVERT_JSON_TO_FDB;
            console = true;
        }

        if(config.containsKey("log")) {
            String level = config.get("log");
            switch (level) {
                case "all":
                    logLevel = LogLevel.ALL;
                    break;
                case "none":
                    logLevel = LogLevel.NONE;
                    break;
            }
        }

        switch(role) {
            case CONVERT_FDB_TO_JSON:
                if(!config.containsKey("out")) Converter.convertToJson(config.get("convert-to-json"));
                else Converter.convertToJson(config.get("convert-to-json"), config.get("out"));
                break;
            case CONVERT_JSON_TO_FDB:
                if(!config.containsKey("out")) Converter.convertFromJson(config.get("convert-to-fdb"));
                else Converter.convertToJson(config.get("convert-to-fdb"), config.get("out"));
                break;
            case NORMAL:
                GUI.getGUI().setup();
                break;
        }
    }
}
