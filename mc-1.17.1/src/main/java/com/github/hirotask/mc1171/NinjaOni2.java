package com.github.hirotask.mc1171;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NinjaOni2 extends JavaPlugin {


    @Override
    public void onEnable() {
        NinjaOniAPI.setInstance(this);
        NinjaOniAPI.getInstance().getGame().setup();

        //コマンドの設定
        CommandAPI.registerCommand(NinjaCommand.class);

        //リスナーの設定
        new com.github.hirotask.mc1171.listener.JoinQuitListener(this);
        new com.github.hirotask.mc1171.listener.NinjaMoveListener(this);
        new com.github.hirotask.mc1171.listener.NinjaOniListener(this);
        new com.github.hirotask.mc1171.listener.NinjaItemListener(this);
        new com.github.hirotask.mc1171.listener.ShopListener(this);

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
