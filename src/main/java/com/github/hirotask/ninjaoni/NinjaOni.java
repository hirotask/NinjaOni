package com.github.hirotask.ninjaoni;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.hirotask.ninjaoni.inventory.ItemManager;
import com.github.hirotask.ninjaoni.listener.NinjaItemListener;
import com.github.hirotask.ninjaoni.listener.NinjaMoveListener;
import com.github.hirotask.ninjaoni.listener.NinjaOniListener;
import com.github.hirotask.ninjaoni.listener.ShopListener;
import com.github.hirotask.ninjaoni.utils.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NinjaOni extends JavaPlugin {

    @Getter
    private Game game = null;

    @Getter
    private ProtocolManager protocolManager;

    @Getter
    private NinjaManager ninjaManager = null;
    @Getter
    private ItemManager itemManager = null;
    @Getter
    private Config myConfig = null;

    @Override
    public void onEnable() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.ninjaManager = new NinjaManager(this);
        this.itemManager = new ItemManager(this);
        this.myConfig = new Config(this);

        //リスナーの設定
        new NinjaMoveListener(this);
        new NinjaOniListener(this);
        new NinjaItemListener(this);
        new ShopListener(this);

        // Runnable の設定
        this.game = new Game(this);
        this.game.setup();

        //コマンドの設定
        new NinjaCommand(this).registerAllCommands();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
