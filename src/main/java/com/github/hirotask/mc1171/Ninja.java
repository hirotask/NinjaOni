package com.github.hirotask.mc1171;

import com.github.hirotask.mc1171.inventory.item.NinjaItem;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Data
public class Ninja {

    private Player player;
    private boolean isClimbing; //壁を上っているか
    private com.github.hirotask.mc1171.Game.Teams team; //所属チーム
    private boolean isLocked; //捕まっているか
    private int hp;
    private int money;

    private java.util.List<com.github.hirotask.mc1171.inventory.item.NinjaItem> items;

    public Ninja(Player player, boolean isClimbing, boolean isLocked, int hp, com.github.hirotask.mc1171.Game.Teams team, int money) {
        this.player = player;
        this.isClimbing = isClimbing;
        this.isLocked = isLocked;
        this.hp = hp;
        this.money = money;
        this.team = team;
        this.items = new java.util.ArrayList<>();
    }

    public Ninja(Player player, com.github.hirotask.mc1171.Game.Teams team) {
        this(player,false,false, 60, team, 0);
    }

    public void decHP() {
        this.hp -= 1;
    }

    public void incMoney() {
        this.money += 1;
    }

    public void decMoney() {
        this.money -= 1;
    }

    public void setLocked(boolean b) {
        this.isLocked = b;

        if(b) {
            com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getGame().addEntry(this.player, com.github.hirotask.mc1171.Game.Teams.LOCKEDPLAYER);
        }else {
            com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getGame().addEntry(this.player, com.github.hirotask.mc1171.Game.Teams.PLAYER);
        }
    }

    public void addNinjaItem(com.github.hirotask.mc1171.inventory.item.NinjaItem item) {
        if(this.items.size() < 4) {
            this.items.add(item);
            getPlayer().sendMessage(item.name() + "を追加しました");
        }
    }
}
