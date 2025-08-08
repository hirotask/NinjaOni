package com.github.hirotask.ninjaoni.inventory.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public interface NinjaItem {

    //Material
    Material type();

    //付与エンチャント
    java.util.HashMap<Enchantment,Integer> enchants();

    //アイテム名
    String name();

    //Lore
    default java.util.List<String> lore() {
        return new java.util.ArrayList<>();
    }

    //アイテム実行処理
    void execute(com.github.hirotask.ninjaoni.Ninja ninja);

    com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType ninjaItemType();

    enum NinjaItemType {
        ONI_ITEM,PLAYER_ITEM
    }
}
