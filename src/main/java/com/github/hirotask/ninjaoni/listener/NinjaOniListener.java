package com.github.hirotask.ninjaoni.listener;

import com.github.hirotask.ninjaoni.event.OniTouchNinjaEvent;
import com.github.hirotask.ninjaoni.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * 忍者鬼のゲーム中の仕様に関わるイベントリスナー
 */
public class NinjaOniListener implements Listener {

    public NinjaOniListener(com.github.hirotask.ninjaoni.NinjaOni plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTouchNinja(OniTouchNinjaEvent e) {
        if(!e.getNinja().isLocked()) {
            Player ninjaPlayer = e.getNinja().getPlayer();
            Player oniPlayer = e.getOni().getPlayer();

            ninjaPlayer.playSound(ninjaPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.3F, 1);
            MessageManager.sendAll(ChatColor.RED + ninjaPlayer.getName() + ChatColor.WHITE + "は" + ChatColor.DARK_AQUA + oniPlayer.getName() + ChatColor.WHITE + "に確保された！");
            e.getNinja().setLocked(true);
        }
    }

}