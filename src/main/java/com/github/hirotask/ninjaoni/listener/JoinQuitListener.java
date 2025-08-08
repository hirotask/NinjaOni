package com.github.hirotask.ninjaoni.listener;

import com.github.hirotask.ninjaoni.Ninja;
import com.github.hirotask.ninjaoni.NinjaOni;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final NinjaOni plugin;

    public JoinQuitListener(com.github.hirotask.ninjaoni.NinjaOni plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        this.plugin.getNinjaManager().addNinjaPlayer(new Ninja(this.plugin, player, com.github.hirotask.ninjaoni.Game.Teams.PLAYER));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        this.plugin.getNinjaManager().ninjaPlayers.removeIf(np -> np.getPlayer().getName().equals(player.getName()));
    }
}