package com.github.hirotask.mc1171.listener;

import com.github.hirotask.mc1171.Game;
import com.github.hirotask.mc1171.NinjaManager;
import com.github.hirotask.mc1171.NinjaOni2;
import com.github.hirotask.mc1171.Ninja;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    public JoinQuitListener(com.github.hirotask.mc1171.NinjaOni2 plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        com.github.hirotask.mc1171.NinjaManager.getInstance().addNinjaPlayer(new com.github.hirotask.mc1171.Ninja(player, com.github.hirotask.mc1171.Game.Teams.PLAYER));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        com.github.hirotask.mc1171.NinjaManager.getInstance().ninjaPlayers.removeIf(np -> np.getPlayer().getName().equals(player.getName()));
    }
}