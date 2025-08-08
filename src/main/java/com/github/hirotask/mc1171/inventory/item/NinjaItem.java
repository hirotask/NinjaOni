package com.github.hirotask.mc1171.inventory.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    void execute(com.github.hirotask.mc1171.Ninja ninja);

    com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType ninjaItemType();

    enum NinjaItemType {
        ONI_ITEM,PLAYER_ITEM
    }
}
