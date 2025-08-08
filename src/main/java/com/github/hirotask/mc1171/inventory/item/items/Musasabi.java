package com.github.hirotask.mc1171.inventory.item.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Musasabi implements com.github.hirotask.mc1171.inventory.item.NinjaItem {

    @Override
    public Material type() {
        return Material.LEATHER;
    }

    @Override
    public java.util.HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "ムササビ";
    }

    @Override
    public void execute(com.github.hirotask.mc1171.Ninja ninja) {
        Player player = ninja.getPlayer();
        Location loc = player.getLocation().clone();
        Location effectLoc = loc.add(0,-1,0);

        Vector vec = loc.getDirection().normalize().setY(0).multiply(1.2);

        player.spawnParticle(Particle.SPELL_MOB,effectLoc,5 );
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 10,1));
        player.setVelocity(vec);
    }

    @Override
    public com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType ninjaItemType() {
        return com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType.PLAYER_ITEM;
    }
}
