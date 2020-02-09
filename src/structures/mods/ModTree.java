package structures.mods;

import java.util.HashMap;
import java.util.Map;

/**
 * this class should basically hold a tree of mods and their dependencies
 */
public class ModTree {
    private HashMap<String, Mod> mods;

    public ModTree() {
        this.mods = new HashMap<>();
    }

    public void addMod(Mod mod) {
        this.mods.put(mod.getExtractedHash(), mod);
    }

    public ModList flatten() {
        ModList modList = new ModList();
        for(Map.Entry<String, Mod> entry : this.mods.entrySet()) {
            modList.addMod(entry.getValue());
        }
        return modList;
    }
}
