package structures.mods;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * this class should basically return a flat listing of mods and their dependencies
 */
public class ModList implements Serializable {
    private HashMap<String, Mod> mods;

    public ModList() {
        this.mods = new HashMap<>();
    }

    public void addMod(Mod mod) {
        this.mods.put(mod.getExtractedHash(), mod);
    }

    public HashMap<String, Mod> getMods() {
        HashMap<String, Mod> modsList = new HashMap<>();
        for(Map.Entry<String, Mod> entry : this.mods.entrySet()) {
            modsList.put(entry.getKey(), entry.getValue());
            modsList.putAll(getMods(entry.getValue()));
        }
        return modsList;
    }

    private HashMap<String, Mod> getMods(Mod mod) {
        HashMap<String, Mod> modsList = new HashMap<>();
        for(Map.Entry<String, Mod> entry : mod.getDependencies().entrySet()) {
            modsList.put(entry.getKey(), entry.getValue());
            modsList.putAll(getMods(entry.getValue()));
        }
        return modsList;
    }
}
