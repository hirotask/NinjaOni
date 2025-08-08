package com.github.hirotask.ninjaoni.inventory.item.items;

import com.github.hirotask.ninjaoni.NinjaOni;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Kemuri implements com.github.hirotask.ninjaoni.inventory.item.NinjaItem {

    private final NinjaOni plugin;

    public Kemuri(NinjaOni plugin) {
        this.plugin = plugin;
    }

    @Override
    public Material type() {
        return Material.FIREWORK_STAR;
    }

    @Override
    public java.util.HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "煙玉";
    }

    @Override
    public com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType ninjaItemType() {
        return com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType.PLAYER_ITEM;
    }

    @Override
    public void execute(com.github.hirotask.ninjaoni.Ninja ninja) {
        Player player = ninja.getPlayer();

        Slime slime = (Slime) player.getWorld().spawnEntity(player.getLocation(), EntityType.SLIME);
        slime.setSize(1);
        slime.setCanPickupItems(false);
        slime.setInvisible(true);

        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 2, 1));

        new BukkitRunnable() {

            int count = 0;

            @Override
            public void run() {
                if (count >= 40) {
                    this.cancel();
                } else {
                    Location location1 = slime.getEyeLocation();
                    Location location2 = slime.getEyeLocation();
                    Location location3 = slime.getEyeLocation();
                    int particles = 30;
                    float radius = 0.7f;
                    for (int i = 0; i < particles; i++) {
                        double angle, x, z;

                        angle = 2 * Math.PI * i / particles;
                        x = Math.cos(angle) * radius;
                        z = Math.sin(angle) * radius;

                        location1.add(x, 0, z);
                        location2.add(x, 2, z);
                        location3.add(x, 5, z);

                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 5, 0, 0, 0);
                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location2, 5, 0, 0, 0);
                        slime.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location3, 5, 0, 0, 0);

                        location1.subtract(x, 0, z);
                        location2.subtract(x, 2, z);
                        location3.subtract(x, 5, z);

                    }
                    count++;
                }
            }
        }.runTaskTimer(this.plugin, 0, 1);
    }


}
