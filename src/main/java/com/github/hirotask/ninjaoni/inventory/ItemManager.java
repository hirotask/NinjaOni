package com.github.hirotask.ninjaoni.inventory;

import com.github.hirotask.ninjaoni.NinjaOni;
import java.util.List;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public final class ItemManager {

    @Getter
    public final List<com.github.hirotask.ninjaoni.inventory.item.NinjaItem> ninjaItems = new java.util.ArrayList<>();
    @Getter
    public final List<ItemStack> items = new java.util.ArrayList<>();

    public ItemManager(NinjaOni ninjaOni) {
        ninjaItems.add(new com.github.hirotask.ninjaoni.inventory.item.items.Kageoi(ninjaOni));
        ninjaItems.add(new com.github.hirotask.ninjaoni.inventory.item.items.Kakure());
        ninjaItems.add(new com.github.hirotask.ninjaoni.inventory.item.items.Kemuri(ninjaOni));
        ninjaItems.add(new com.github.hirotask.ninjaoni.inventory.item.items.Kunai());
        ninjaItems.add(new com.github.hirotask.ninjaoni.inventory.item.items.Nenchaku());
        ninjaItems.add(new com.github.hirotask.ninjaoni.inventory.item.items.Shukuchi(ninjaOni));
        ninjaItems.add(new com.github.hirotask.ninjaoni.inventory.item.items.Choyaku());
        ninjaItems.add(new com.github.hirotask.ninjaoni.inventory.item.items.Musasabi());
    }

    public ItemStack getItem(com.github.hirotask.ninjaoni.inventory.item.NinjaItem ninjaItem) {
        ItemStack item = new ItemStack(ninjaItem.type());

        if(ninjaItem.enchants() != null) {
            for(Enchantment enchant : ninjaItem.enchants().keySet()) {
                int level = ninjaItem.enchants().get(enchant);
                item.addEnchantment(enchant,level);
            }
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ninjaItem.name());
        meta.setUnbreakable(true);
        meta.setLore(ninjaItem.lore());
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItem(Material material,String name, Enchantment enchant, int level ) {
        ItemStack item = new ItemStack(material);
        item.addEnchantment(enchant,level);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack createPlayerHead(String player_name, String item_name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD,1);

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName(item_name);
        meta.setOwner(player_name);

        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getOniHelmet() {
        return createItem(Material.DIAMOND_HELMET, "鬼ヘルメット", Enchantment.BINDING_CURSE, 1);
    }

    public static ItemStack getOniChestplate() {
        return createItem(Material.DIAMOND_CHESTPLATE, "鬼チェストプレート", Enchantment.BINDING_CURSE, 1);
    }

    public static ItemStack getOniLeggings() {
        return createItem(Material.DIAMOND_LEGGINGS, "鬼レギンス", Enchantment.BINDING_CURSE, 1);
    }

    public static ItemStack getOniBoots() {
        return createItem(Material.DIAMOND_BOOTS, "鬼ブーツ", Enchantment.BINDING_CURSE, 1);
    }

    public static ItemStack getMoney() {
       return createPlayerHead("MrSnowDK", "お金");
    }
}
