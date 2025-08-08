package com.github.hirotask.ninjaoni.runnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountDownTask extends BukkitRunnable {
    private int count;
    private com.github.hirotask.ninjaoni.NinjaOni2 plugin;
    private final String symbol_left = "&e>";
    private final String symbol_right = "&e<";

    public CountDownTask(int count) {
        this.plugin = com.github.hirotask.ninjaoni.NinjaOniAPI.INSTANCE.getPlugin();

        if(count > 0) {
            this.count = count;
        } else {
            this.count = com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getMyConfig().getCountdownTime();
        }
    }

    @Override
    public void run() {
        if(com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getGame().getGameState() == com.github.hirotask.ninjaoni.Game.GameState.COUNTDOWN) {
            if(count < 0) {
                this.cancel();
            }

            if (count == 0) {
                com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getGame().setGameState(com.github.hirotask.ninjaoni.Game.GameState.INGAME);
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle("GAME START!", null, 10, 70, 2);
                }
                this.cancel();
            } else {
                StringBuilder sb = new StringBuilder();
                for(int i=0; i < count; i++) {
                    sb.append(ChatColor.translateAlternateColorCodes('&',symbol_left));
                }

                sb.append(count);

                for(int i=0; i < count; i++) {
                    sb.append(ChatColor.translateAlternateColorCodes('&',symbol_right));
                }

                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle(sb.toString(), null, 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1);
                }
            }
            count--;
        }
    }
}
