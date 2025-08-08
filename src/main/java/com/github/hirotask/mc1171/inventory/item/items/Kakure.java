package com.github.hirotask.mc1171.inventory.item.items;

import com.github.hirotask.mc1171.inventory.item.NinjaItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Kakure implements com.github.hirotask.mc1171.inventory.item.NinjaItem {

    @Override
    public Material type() {
        return Material.SLIME_BALL;
    }

    @Override
    public java.util.HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "隠れ玉";
    }

    @Override
    public void execute(com.github.hirotask.mc1171.Ninja ninja) {
        Player player = ninja.getPlayer();

        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 10, 1));
    }

    @Override
    public com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType ninjaItemType() {
        return com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType.PLAYER_ITEM;
    }
}
