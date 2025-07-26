package com.github.hirotask.mc1171.listener;

import com.github.hirotask.mc1171.Game;
import com.github.hirotask.mc1171.NinjaManager;
import com.github.hirotask.mc1171.NinjaOni2;
import com.github.hirotask.mc1171.inventory.ItemManager;
import com.github.hirotask.mc1171.inventory.item.NinjaItem;
import com.github.hirotask.mc1171.Ninja;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopListener implements Listener {

    public ShopListener(NinjaOni2 plugin) {
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onInteractVillager(PlayerInteractEntityEvent e) {
        if(!(e.getRightClicked() instanceof Villager)) {
           return;
        }

        Villager villager = (Villager) e.getRightClicked();
        Player player = e.getPlayer();
        ItemManager im = new ItemManager();
        if(villager.getCustomName() != null) {
            String customName = ChatColor.stripColor(villager.getCustomName());
            if(customName.equals("鬼専用ショップ")) {
                Inventory inv = Bukkit.createInventory(null, 9, customName);

                List<ItemStack> items = new ArrayList<>();

                for(NinjaItem ni : im.getNinjaItems()) {
                    if(ni.ninjaItemType() == NinjaItem.NinjaItemType.ONI_ITEM) {
                        ItemStack is = im.getItem(ni);
                        items.add(is);
                    }
                }

                int slot = 0;
                for(ItemStack item : items) {
                    inv.setItem(slot, item);
                    slot+= 2;
                    if(slot > 10) {
                        break;
                    }
                }

                player.openInventory(inv);

            } else if(customName.equals("プレイヤー専用ショップ")) {
                Inventory inv = Bukkit.createInventory(null, 9, customName);

                List<ItemStack> items = new ArrayList<>();

                for(NinjaItem ni : im.getNinjaItems()) {
                    if(ni.ninjaItemType() == NinjaItem.NinjaItemType.PLAYER_ITEM) {
                        ItemStack is = im.getItem(ni);
                        items.add(is);
                    }
                }

                int slot = 0;
                for(ItemStack item : items) {
                    inv.setItem(slot, item);
                    slot+= 2;
                    if(slot > 10) {
                        break;
                    }
                }


                player.openInventory(inv);
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        String title = ChatColor.stripColor(e.getView().getTitle());
        ItemStack clickedItem = e.getCurrentItem();

        Player player = (Player) e.getWhoClicked();
        Ninja ninja = null;
        ItemManager im = new ItemManager();

        if(clickedItem == null) {
            return;
        }
        if(!clickedItem.hasItemMeta()) {
            return;
        }

        String itemName = clickedItem.getItemMeta().getDisplayName();

        for(Ninja ninja1 : NinjaManager.getInstance().ninjaPlayers) {
            if(ninja1.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                ninja = ninja1;
                break;
            }
        }

        if(ninja != null) {

            if(title.equals("鬼専用ショップ") && ninja.getTeam() == Game.Teams.ONI) {
                for(NinjaItem ni : im.getNinjaItems()) {
                    if(ni.name().equals(itemName)) {
                        ninja.addNinjaItem(ni);
                    }
                }
            }
            if(title.equals("プレイヤー専用ショップ") && ninja.getTeam() == Game.Teams.PLAYER) {
                for(NinjaItem ni : im.getNinjaItems()) {
                    if(ni.name().equals(itemName)) {
                        ninja.addNinjaItem(ni);
                    }
                }
            }

            NinjaManager.getInstance().updateNinjaPlayer(ninja);
            e.setCancelled(true);
        }
    }
}
