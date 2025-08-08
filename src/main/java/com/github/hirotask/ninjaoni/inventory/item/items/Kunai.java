package com.github.hirotask.ninjaoni.inventory.item.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Kunai implements com.github.hirotask.ninjaoni.inventory.item.NinjaItem {

    @Override
    public Material type() {
        return Material.ARROW;
    }

    @Override
    public java.util.HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "クナイ";
    }

    @Override
    public void execute(com.github.hirotask.ninjaoni.Ninja ninja) {
        Player player = ninja.getPlayer();

        Vector vec = player.getEyeLocation().getDirection().multiply(1.6);
        player.launchProjectile(Arrow.class, vec);
    }

    @Override
    public com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType ninjaItemType() {
        return com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType.ONI_ITEM;
    }
}
