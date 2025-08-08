package com.github.hirotask.ninjaoni.inventory.item.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class Nenchaku implements com.github.hirotask.ninjaoni.inventory.item.NinjaItem {

    @Override
    public Material type() {
        return Material.SNOWBALL;
    }

    @Override
    public java.util.HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "粘着玉";
    }

    @Override
    public void execute(com.github.hirotask.ninjaoni.Ninja ninja) {
        Player player = ninja.getPlayer();
        Location loc = player.getEyeLocation().clone();
        Vector direction = loc.getDirection();

        Vector vec = direction.normalize().multiply(1.5);
        player.launchProjectile(Snowball.class, vec);
    }


    @Override
    public com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType ninjaItemType() {
        return com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType.PLAYER_ITEM;
    }
}
