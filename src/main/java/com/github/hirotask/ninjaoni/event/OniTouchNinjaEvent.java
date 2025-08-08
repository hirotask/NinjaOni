package com.github.hirotask.ninjaoni.event;

import com.github.hirotask.ninjaoni.Ninja;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 鬼が忍者（プレイヤー）をタッチしたときのイベント
 */
public class OniTouchNinjaEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancel = false;

    @Getter
    private final Ninja oni;

    @Getter
    private final Ninja ninja;

    public OniTouchNinjaEvent(Ninja oni, Ninja ninja) {
        this.oni = oni;
        this.ninja = ninja;
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