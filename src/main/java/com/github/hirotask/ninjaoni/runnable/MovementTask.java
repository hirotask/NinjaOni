package com.github.hirotask.ninjaoni.runnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


public class MovementTask extends BukkitRunnable {

    private final com.github.hirotask.ninjaoni.NinjaOni2 plugin = com.github.hirotask.ninjaoni.NinjaOniAPI.INSTANCE.getPlugin();

    @Override
    public void run() {
        if(com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getGame().getGameState() == com.github.hirotask.ninjaoni.Game.GameState.NONE) {
            this.cancel();
        }

        if(com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getGame().getGameState() != com.github.hirotask.ninjaoni.Game.GameState.INGAME) {
            return;
        }

        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(!com.github.hirotask.ninjaoni.NinjaManager.getInstance().containsNinja(player)) return;

            com.github.hirotask.ninjaoni.Ninja ninja = com.github.hirotask.ninjaoni.NinjaManager.getInstance().getNinjaPlayer(player);

            if(ninja.getTeam() == com.github.hirotask.ninjaoni.Game.Teams.PLAYER) {
                if(ninja.isLocked()) { //捕まっている時の処理
                    if(ninja.getHp() > 0) {
                        ninja.decHP();
                        ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.ENTITY_PLAYER_HURT, 0.3F, 1);
                        ninja.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20 * 2, 4));
                        ninja.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, -100), true);
                        ninja.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 2, 3));
                    } else {
                        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                            p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5F,1);
                        }

                        com.github.hirotask.ninjaoni.utils.MessageManager.sendAll(ChatColor.RED + ninja.getPlayer().getName() + "が脱落した");
                        ninja.setTeam(com.github.hirotask.ninjaoni.Game.Teams.SPECTATOR);
                        com.github.hirotask.ninjaoni.NinjaManager.getInstance().updateNinjaPlayer(ninja);
                        com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getGame().addEntry(ninja.getPlayer(), com.github.hirotask.ninjaoni.Game.Teams.SPECTATOR);
                        ninja.getPlayer().setGameMode(GameMode.SPECTATOR);
                    }
                }
            }
        }
    }
}
