package com.github.hirotask.ninjaoni;

import com.github.hirotask.ninjaoni.inventory.ItemManager;
import com.github.hirotask.ninjaoni.inventory.item.NinjaItem;
import com.github.hirotask.ninjaoni.utils.MessageManager;
import java.util.HashMap;
import java.util.List;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Data
public class Ninja {

    private NinjaOni ninjaOni;

    private Player player;
    private boolean isClimbing; //壁を上っているか
    private Game.Teams team; //所属チーム
    private boolean isLocked; //捕まっているか
    private int hp;
    private int money;

    private List<NinjaItem> items;

    public Ninja(NinjaOni ninjaOni, Player player, boolean isClimbing, boolean isLocked, int hp, com.github.hirotask.ninjaoni.Game.Teams team, int money) {
        this.ninjaOni = ninjaOni;
        this.player = player;
        this.isClimbing = isClimbing;
        this.isLocked = isLocked;
        this.hp = hp;
        this.money = money;
        this.team = team;
        this.items = new java.util.ArrayList<>();
    }

    public Ninja(NinjaOni ninjaOni, Player player, Game.Teams team) {
        this(ninjaOni, player,false,false, 60, team, 0);
    }

    public void decHP() {
        this.hp -= 1;
    }

    public void incMoney() {
        this.money += 1;
    }

    public void decMoney() {
        if (this.money > 0) {
            PlayerInventory inv = this.player.getInventory();

            if (inv.contains(ItemManager.getMoney().getType())) {
                HashMap<Integer, ? extends ItemStack> indexs = inv.all(ItemManager.getMoney().getType());
                for (int key : indexs.keySet()) {
                    if (key == 18) {
                        int amount = inv.getItem(key).getAmount();
                        if (amount > 1) {
                            inv.getItem(key).setAmount(inv.getItem(key).getAmount() - 1);
                        } else {
                            inv.remove(inv.getItem(key));
                        }
                    }

                }
            }
            this.money--;
        }
    }

    public void setLocked(boolean b) {
        this.isLocked = b;

        if(b) {
            this.ninjaOni.getGame().addEntry(this.player, com.github.hirotask.ninjaoni.Game.Teams.LOCKEDPLAYER);
        }else {
            this.ninjaOni.getGame().addEntry(this.player, com.github.hirotask.ninjaoni.Game.Teams.PLAYER);
        }
    }

    public void addNinjaItem(com.github.hirotask.ninjaoni.inventory.item.NinjaItem item) {
        if(this.items.size() < 4) {
            this.items.add(item);
            getPlayer().sendMessage(item.name() + "を追加しました");
        }
    }

    public void purchaseNinjaItem(NinjaItem item) {
        if(this.money > 0) {
            this.decMoney();
            this.addNinjaItem(item);
            this.player.playSound(this.player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.3F, 1);
        }
    }

    public void useNinjaItem(NinjaItem item) {
        Inventory inv = this.player.getInventory();

        if(inv.contains(item.type())) {
            HashMap<Integer, ? extends ItemStack> indexs = inv.all(item.type());

            for (int key : indexs.keySet()) {
                if (key >= 0 && key <= 8) {
                    int amount = inv.getItem(key).getAmount();
                    if (amount > 1) {
                        inv.getItem(key).setAmount(inv.getItem(key).getAmount() - 1);
                    } else {
                        inv.remove(inv.getItem(key));
                    }

                    break;
                }
            }

            item.execute(this);

        }
    }

    public boolean containsUnableClimbBlocks(Material m) {
        List<Material> unableClimbBlocks = new java.util.ArrayList<Material>();

        unableClimbBlocks.add(Material.LADDER);
        unableClimbBlocks.add(Material.VINE);

        for(Material material : Material.values()) {
            if(material.name().endsWith("SLAB")
                    || material.name().endsWith("CARPET")) {
                unableClimbBlocks.add(material);
            }
        }

        return unableClimbBlocks.contains(m);
    }

    /**
     * 忍者を捕まえる
     */
    public void touch(Ninja ninja) {
        if (this.team == Game.Teams.ONI && ninja.getTeam() == Game.Teams.PLAYER) {
            if(!ninja.isLocked()) {
                Player ninjaPlayer = ninja.getPlayer();
                Player oniPlayer = this.getPlayer();

                ninjaPlayer.playSound(ninjaPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.3F, 1);
                MessageManager.sendAll(ChatColor.RED + ninjaPlayer.getName() + ChatColor.WHITE + "は" + ChatColor.DARK_AQUA + oniPlayer.getName() + ChatColor.WHITE + "に確保された！");
                ninja.setLocked(true);
            }
        }
    }

}
