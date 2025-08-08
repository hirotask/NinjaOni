package com.github.hirotask.ninjaoni.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * プレイヤーがショップ村人をクリックした時に発火するイベント
 *
 */
public class ShopOpenEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancel = false;

    @Getter
    private final Player player;

    @Getter
    private final String shopName;

    public ShopOpenEvent(Player player, String shopName) {
        this.player = player;
        this.shopName = shopName;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
