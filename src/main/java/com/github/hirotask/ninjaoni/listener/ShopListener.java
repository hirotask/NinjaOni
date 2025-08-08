package com.github.hirotask.ninjaoni.listener;

import com.github.hirotask.ninjaoni.Ninja;
import com.github.hirotask.ninjaoni.NinjaOni;
import com.github.hirotask.ninjaoni.inventory.ItemManager;
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

public class ShopListener implements Listener {

    private final NinjaOni plugin;

    public ShopListener(NinjaOni plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteractVillager(PlayerInteractEntityEvent e) {
        if(!(e.getRightClicked() instanceof Villager)) {
           return;
        }

        Villager villager = (Villager) e.getRightClicked();
        Player player = e.getPlayer();
        com.github.hirotask.ninjaoni.inventory.ItemManager im = new com.github.hirotask.ninjaoni.inventory.ItemManager(this.plugin);
        if(villager.getCustomName() != null) {
            String customName = ChatColor.stripColor(villager.getCustomName());
            if(customName.equals("鬼専用ショップ")) {
                Inventory inv = Bukkit.createInventory(null, 9, customName);

                java.util.List<ItemStack> items = new java.util.ArrayList<>();

                for(com.github.hirotask.ninjaoni.inventory.item.NinjaItem ni : im.getNinjaItems()) {
                    if(ni.ninjaItemType() == com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType.ONI_ITEM) {
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

                java.util.List<ItemStack> items = new java.util.ArrayList<>();

                for(com.github.hirotask.ninjaoni.inventory.item.NinjaItem ni : im.getNinjaItems()) {
                    if(ni.ninjaItemType() == com.github.hirotask.ninjaoni.inventory.item.NinjaItem.NinjaItemType.PLAYER_ITEM) {
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
        com.github.hirotask.ninjaoni.Ninja ninja = null;
        com.github.hirotask.ninjaoni.inventory.ItemManager im = new ItemManager(this.plugin);

        if(clickedItem == null) {
            return;
        }
        if(!clickedItem.hasItemMeta()) {
            return;
        }

        String itemName = clickedItem.getItemMeta().getDisplayName();

        for(Ninja ninja1 : this.plugin.getNinjaManager().ninjaPlayers) {
            if(ninja1.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                ninja = ninja1;
                break;
            }
        }

        if(ninja != null) {

            if(title.equals("鬼専用ショップ") && ninja.getTeam() == com.github.hirotask.ninjaoni.Game.Teams.ONI) {
                for(com.github.hirotask.ninjaoni.inventory.item.NinjaItem ni : im.getNinjaItems()) {
                    if(ni.name().equals(itemName)) {
                        ninja.addNinjaItem(ni);
                    }
                }
            }
            if(title.equals("プレイヤー専用ショップ") && ninja.getTeam() == com.github.hirotask.ninjaoni.Game.Teams.PLAYER) {
                for(com.github.hirotask.ninjaoni.inventory.item.NinjaItem ni : im.getNinjaItems()) {
                    if(ni.name().equals(itemName)) {
                        ninja.addNinjaItem(ni);
                    }
                }
            }

            this.plugin.getNinjaManager().updateNinjaPlayer(ninja);
            e.setCancelled(true);
        }
    }
}
