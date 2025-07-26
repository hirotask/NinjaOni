package com.github.hirotask.mc1171;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.hirotask.mc1171.inventory.ItemManager;
import lombok.Getter;
import org.bukkit.command.Command;

/* 提供機能全てが使えるシングルトン */
public enum NinjaOniAPI {
    INSTANCE;

    @Getter
    private NinjaOni2 plugin;

    @Getter
    private Game game;

    @Getter
    private com.github.hirotask.mc1171.utils.Config myConfig;

    @Getter
    private ProtocolManager protocol;

    @Getter
    private ItemManager itemManager;

    @Getter
    private NinjaManager ninjaManager = NinjaManager.getInstance();


    public static void setInstance(NinjaOni2 plugin) {
        INSTANCE.plugin = plugin;
    }

    public static NinjaOniAPI getInstance() {
        if(INSTANCE.plugin == null) {
            return null;
        }

        if(INSTANCE.game == null) {
            INSTANCE.game = new Game();
        }
        if(INSTANCE.myConfig == null) {
            INSTANCE.myConfig = new com.github.hirotask.mc1171.utils.Config(INSTANCE.plugin);
        }
        if(INSTANCE.protocol == null) {
            INSTANCE.protocol = ProtocolLibrary.getProtocolManager();
        }
        if(INSTANCE.itemManager == null) {
            INSTANCE.itemManager = new ItemManager();
        }

        return INSTANCE;
    }
}
