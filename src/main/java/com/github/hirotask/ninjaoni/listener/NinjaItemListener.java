package com.github.hirotask.ninjaoni.listener;

import com.github.hirotask.ninjaoni.Game;
import com.github.hirotask.ninjaoni.Ninja;
import com.github.hirotask.ninjaoni.NinjaOni;
import com.github.hirotask.ninjaoni.inventory.ItemManager;
import com.github.hirotask.ninjaoni.inventory.item.NinjaItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * 忍者専用アイテムに関するリスナー
 */
public class NinjaItemListener implements Listener {
    private NinjaOni plugin;

    public NinjaItemListener(NinjaOni plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteractItem(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        PlayerInventory inv = player.getInventory();
        ItemStack item = inv.getItemInMainHand();
        ItemManager itemManager = this.plugin.getItemManager();

        // 専用アイテム使用かどうかをチェックする
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (this.plugin.getNinjaManager().containsNinja(player)) {
                Ninja ninja = this.plugin.getNinjaManager().getNinjaPlayer(player);

                for (NinjaItem ninjaItem : itemManager.getNinjaItems()) {
                    if (item.getType() != ninjaItem.type()) {
                        continue;
                    }

                    ninja.useNinjaItem(ninjaItem);
                    break;
                }

            }
        }
    }

    @EventHandler
    public void onInteractVillager(PlayerInteractEntityEvent e) {
        if(!(e.getRightClicked() instanceof Villager)) {
            return;
        }

        Villager villager = (Villager) e.getRightClicked();
        Player player = e.getPlayer();
        ItemManager im = new com.github.hirotask.ninjaoni.inventory.ItemManager(this.plugin);
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
                        ninja.purchaseNinjaItem(ni);
                    }
                }
            }
            if(title.equals("プレイヤー専用ショップ") && ninja.getTeam() == com.github.hirotask.ninjaoni.Game.Teams.PLAYER) {
                for(com.github.hirotask.ninjaoni.inventory.item.NinjaItem ni : im.getNinjaItems()) {
                    if(ni.name().equals(itemName)) {
                        ninja.purchaseNinjaItem(ni);
                    }
                }
            }

            this.plugin.getNinjaManager().updateNinjaPlayer(ninja);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (this.plugin.getGame().getGameState() != Game.GameState.INGAME) {
            return;
        }

        //クナイの処理
        if (e.getEntity() instanceof Arrow) {
            if (e.getHitEntity() == null) {
                e.getEntity().remove();
                e.setCancelled(true);
            } else {
                Arrow arrow = (Arrow) e.getEntity();
                Player player = (Player) e.getHitEntity();

                if(arrow.getShooter() == null) {
                    return;
                }

                if(!(arrow.getShooter() instanceof Player)) {
                    return;
                }

                if(!this.plugin.getNinjaManager().containsNinja(player)) {
                    return;
                }

                Player shooter = (Player) arrow.getShooter();
                com.github.hirotask.ninjaoni.Ninja ninja = this.plugin.getNinjaManager().getNinjaPlayer(player);

                if(ninja.getTeam() != com.github.hirotask.ninjaoni.Game.Teams.PLAYER) {
                    return;
                }

                if(!ninja.isLocked()) {
                    e.setCancelled(true);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.3F, 1);
                    com.github.hirotask.ninjaoni.utils.MessageManager.sendAll(ChatColor.RED + ninja.getPlayer().getName() + ChatColor.WHITE + "は" + ChatColor.DARK_AQUA + shooter.getName() + ChatColor.WHITE + "に確保された！");
                    ninja.setLocked(true);
                }
            }
        }

        //粘着玉の処理
        if(e.getEntity() instanceof Snowball) {
            if (e.getHitEntity() == null) {
                e.getEntity().remove();
                e.setCancelled(true);
            } else {
                Snowball snowball = (Snowball) e.getEntity();
                Player player = (Player) e.getHitEntity();

                if(snowball.getShooter() == null) {
                    return;
                }

                if(!(snowball.getShooter() instanceof Player)) {
                    return;
                }

                if(!this.plugin.getNinjaManager().containsNinja(player)) {
                    return;
                }

                Player shooter = (Player) snowball.getShooter();
                com.github.hirotask.ninjaoni.Ninja ninja = this.plugin.getNinjaManager().getNinjaPlayer(player);

                if(ninja.getTeam() != com.github.hirotask.ninjaoni.Game.Teams.ONI) {
                    return;
                }

                shooter.playSound(shooter.getLocation(),Sound.BLOCK_SLIME_BLOCK_STEP, 0.8f,1f);
                player.playSound(player.getLocation(),Sound.BLOCK_SLIME_BLOCK_STEP, 0.8f,1f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 4),true);
            }
        }
    }

}
