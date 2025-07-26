package com.github.hirotask.mc1171.utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class Config {
    private final Plugin plugin;
    private FileConfiguration config;

    @Getter
    private int countdownTime, gameTime;

    @Getter
    private Material warpBlockTypeOni, warpBlockTypeSpec;

    @Getter
    private Location TPLocationOni, TPLocationPlayer;

    @Getter
    private int moneyAmount;

    public Config(Plugin plugin) {
        this.plugin = plugin;

        load();
    }

    private void load() {
        plugin.saveDefaultConfig();

        if (Objects.nonNull(config)) {
            reload();
        }

        config = plugin.getConfig();

        countdownTime = config.getInt("countdown-time");
        gameTime = config.getInt("game-time");

        String temp = config.getString("warp-block-type-oni");
        warpBlockTypeOni = Material.valueOf(temp);
        temp = config.getString("warp-block-type-spec");
        warpBlockTypeSpec = Material.valueOf(temp);

        double ox = config.getDouble("tp-location.oni.x");
        double oy = config.getDouble(("tp-location.oni.y"));
        double oz = config.getDouble(("tp-location.oni.z"));
        TPLocationOni = new Location(null, ox,oy,oz);

        double px = config.getDouble("tp-location.player.x");
        double py = config.getDouble(("tp-location.player.y"));
        double pz = config.getDouble(("tp-location.player.z"));
        TPLocationPlayer = new Location(null, px,py,pz);

        moneyAmount = config.getInt("money-amount", 20);

    }

    public void reload() {
        plugin.reloadConfig();
    }

}
