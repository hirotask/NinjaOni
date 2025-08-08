package com.github.hirotask.ninjaoni;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NinjaOni2 extends JavaPlugin {


    @Override
    public void onEnable() {
        com.github.hirotask.ninjaoni.NinjaOniAPI.setInstance(this);
        com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getGame().setup();

        //コマンドの設定
        CommandAPI.registerCommand(com.github.hirotask.ninjaoni.NinjaCommand.class);

        //リスナーの設定
        new com.github.hirotask.ninjaoni.listener.JoinQuitListener(this);
        new com.github.hirotask.ninjaoni.listener.NinjaMoveListener(this);
        new com.github.hirotask.ninjaoni.listener.NinjaOniListener(this);
        new com.github.hirotask.ninjaoni.listener.NinjaItemListener(this);
        new com.github.hirotask.ninjaoni.listener.ShopListener(this);

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
