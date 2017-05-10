package mekfarm;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by CF on 2017-05-10.
 */
public class MekfarmConfig {
    private Configuration config;

    private boolean _allowMachinesToSpawnItems;

    public MekfarmConfig(File configurationFile) {
        this.config = new Configuration(configurationFile);
        this.update();
    }

    public boolean allowMachinesToSpawnItems() {
        return this._allowMachinesToSpawnItems;
    }

    private void update() {
        try {
            this.config.load();
            this._allowMachinesToSpawnItems = this.config.getBoolean(
                    "allowMachinesToSpawnItems",
                    Configuration.CATEGORY_GENERAL,
                    true,
                    "Specified if mekfarm machines are allowed to spawn items in world in case their output inventory is full.\nWarning: the items will be lost if not spawned in the world (WIP)."
            );
        }
        finally {
            if (this.config.hasChanged()) {
                this.config.save();
            }
        }
    }
}
