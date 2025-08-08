package com.github.hirotask.mc1171;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum NinjaManager {

    INSTANCE;

    public java.util.List<Ninja> ninjaPlayers;

    public static com.github.hirotask.mc1171.NinjaManager getInstance() {
        if(INSTANCE.ninjaPlayers == null) {
            INSTANCE.ninjaPlayers = new java.util.ArrayList<>();
        }

        return INSTANCE;
    }

    public boolean containsNinja(Player player) {
        boolean result = false;

        for (com.github.hirotask.mc1171.Ninja np : INSTANCE.ninjaPlayers) {
            if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                result = true;
            }
        }

        return result;
    }

    public void addNinjaPlayer(com.github.hirotask.mc1171.Ninja ninja) {
        if (!containsNinja(ninja.getPlayer())) {
            INSTANCE.ninjaPlayers.add(ninja);
        }
    }

    public com.github.hirotask.mc1171.Ninja getNinjaPlayer(Player player) {
        com.github.hirotask.mc1171.Ninja result = null;

        if (containsNinja(player)) {
            for (com.github.hirotask.mc1171.Ninja np : INSTANCE.ninjaPlayers) {
                if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    result = np;
                }

            }
        }

        return result;
    }

    public void updateNinjaPlayer(com.github.hirotask.mc1171.Ninja ninja) {
        if (!containsNinja(ninja.getPlayer())) {
            return;
        }

        com.github.hirotask.mc1171.Ninja oldNinja = getNinjaPlayer(ninja.getPlayer());
        oldNinja.setClimbing(ninja.isClimbing());
        oldNinja.setTeam(ninja.getTeam());
        com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getGame().addEntry(ninja.getPlayer(), ninja.getTeam());
    }

    public int countNinja(com.github.hirotask.mc1171.Game.Teams team) {
        int result = 0;
        for (com.github.hirotask.mc1171.Ninja ninja : INSTANCE.ninjaPlayers) {
            if (ninja.getTeam() == team) {
                result++;
            }
        }

        return result;
    }

}
