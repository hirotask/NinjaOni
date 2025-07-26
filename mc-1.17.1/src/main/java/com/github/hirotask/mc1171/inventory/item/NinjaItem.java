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
    HashMap<Enchantment,Integer> enchants();

    //アイテム名
    String name();

    //Lore
    default List<String> lore() {
        return new ArrayList<>();
    }

    //アイテム実行処理
    void execute(com.github.hirotask.mc1171.Ninja ninja);

    NinjaItemType ninjaItemType();

    enum NinjaItemType {
        ONI_ITEM,PLAYER_ITEM
    }
}
