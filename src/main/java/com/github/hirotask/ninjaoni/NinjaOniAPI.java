package com.github.hirotask.ninjaoni;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.hirotask.ninjaoni.inventory.ItemManager;
import lombok.Getter;

/* 提供機能全てが使えるシングルトン */
public enum NinjaOniAPI {
    INSTANCE;

    @Getter
    private NinjaOni2 plugin;

    @Getter
    private Game game;

    @Getter
    private com.github.hirotask.ninjaoni.utils.Config myConfig;

    @Getter
    private ProtocolManager protocol;

    @Getter
    private ItemManager itemManager;

    @Getter
    private NinjaManager ninjaManager = com.github.hirotask.ninjaoni.NinjaManager.getInstance();


    public static void setInstance(com.github.hirotask.ninjaoni.NinjaOni2 plugin) {
        INSTANCE.plugin = plugin;
    }

    public static com.github.hirotask.ninjaoni.NinjaOniAPI getInstance() {
        if(INSTANCE.plugin == null) {
            return null;
        }

        if(INSTANCE.game == null) {
            INSTANCE.game = new com.github.hirotask.ninjaoni.Game();
        }
        if(INSTANCE.myConfig == null) {
            INSTANCE.myConfig = new com.github.hirotask.ninjaoni.utils.Config(INSTANCE.plugin);
        }
        if(INSTANCE.protocol == null) {
            INSTANCE.protocol = ProtocolLibrary.getProtocolManager();
        }
        if(INSTANCE.itemManager == null) {
            INSTANCE.itemManager = new com.github.hirotask.ninjaoni.inventory.ItemManager();
        }

        return INSTANCE;
    }
}
