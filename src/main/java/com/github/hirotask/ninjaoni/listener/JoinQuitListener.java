package com.github.hirotask.ninjaoni.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    public JoinQuitListener(com.github.hirotask.ninjaoni.NinjaOni2 plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        com.github.hirotask.ninjaoni.NinjaManager.getInstance().addNinjaPlayer(new com.github.hirotask.ninjaoni.Ninja(player, com.github.hirotask.ninjaoni.Game.Teams.PLAYER));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        com.github.hirotask.ninjaoni.NinjaManager.getInstance().ninjaPlayers.removeIf(np -> np.getPlayer().getName().equals(player.getName()));
    }
}