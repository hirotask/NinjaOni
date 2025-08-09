package com.github.hirotask.ninjaoni.listener;

import com.github.hirotask.ninjaoni.Game;
import com.github.hirotask.ninjaoni.Ninja;
import com.github.hirotask.ninjaoni.NinjaOni;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * 「忍者鬼」を実施するうえで制限を書けなければならない動作を制限するためのリスナー
 */
public class BukkitEventListener implements Listener {
    private final NinjaOni plugin;

    /**
     * GUI が存在するブロック一覧
     */
    private final Material[] GUIBlocks = {
            Material.CRAFTING_TABLE,
            Material.FURNACE,
            Material.FURNACE_MINECART,
            Material.ANVIL,
            Material.BREWING_STAND,
            Material.HOPPER,
            Material.HOPPER_MINECART,
            Material.BEACON,
            Material.ENCHANTING_TABLE,
            Material.CHEST,
            Material.CHEST_MINECART,
            Material.FLETCHING_TABLE,
            Material.ENDER_CHEST,
            Material.DISPENSER,
            Material.DROPPER,
            Material.SMOKER,
            Material.BLAST_FURNACE,
            Material.LOOM,
            Material.BARREL,
            Material.CARTOGRAPHY_TABLE,
            Material.GRINDSTONE,
            Material.SMITHING_TABLE,
            Material.STONECUTTER
    };


    public BukkitEventListener(NinjaOni plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * ログイン時の処理
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        this.plugin.getNinjaManager().addNinjaPlayer(new Ninja(this.plugin, player, com.github.hirotask.ninjaoni.Game.Teams.PLAYER));
    }

    /**
     * ログアウト時の処理
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        this.plugin.getNinjaManager().ninjaPlayers.removeIf(np -> np.getPlayer().getName().equals(player.getName()));
    }

    /**
     * GUIが存在するブロックを開いた時キャンセル
     */
    @EventHandler
    public void onClickGUIBlock(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            for(Material m : GUIBlocks) {
                if(block.getType() == m) {
                    e.setCancelled(true);
                }
            }
        }
    }

    /**
     * ゲーム中壁掛けが壊れないように
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof ItemFrame) {
            if(this.plugin.getGame().getGameState() != Game.GameState.NONE) {
                e.setCancelled(true);
            }
        }
    }

    /**
     *  ゲーム中アーマースタンドが壊れないように
     */
    @EventHandler
    public void onInteractArmorStand(PlayerArmorStandManipulateEvent e) {
        if (this.plugin.getGame().getGameState() != Game.GameState.NONE) {
            e.setCancelled(true);
        }
    }

    /**
     * ゲーム中アイテムを捨てるのを無効化
     */
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if(this.plugin.getGame().getGameState() != Game.GameState.NONE) {
            Player player = e.getPlayer();
            if (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL) {
                ItemStack item = e.getItemDrop().getItemStack().clone();
                e.getItemDrop().remove();
                player.getInventory().addItem(item);
            }
        }
    }

}
