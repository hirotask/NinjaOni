package com.github.hirotask.mc1171.inventory.item.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Kunai implements com.github.hirotask.mc1171.inventory.item.NinjaItem {

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
    public void execute(com.github.hirotask.mc1171.Ninja ninja) {
        Player player = ninja.getPlayer();

        Vector vec = player.getEyeLocation().getDirection().multiply(1.6);
        player.launchProjectile(Arrow.class, vec);
    }

    @Override
    public com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType ninjaItemType() {
        return com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType.ONI_ITEM;
    }
}
