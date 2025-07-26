package com.github.hirotask.mc1171;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum NinjaManager {

    INSTANCE;

    public List<Ninja> ninjaPlayers;

    public static NinjaManager getInstance() {
        if(INSTANCE.ninjaPlayers == null) {
            INSTANCE.ninjaPlayers = new ArrayList<>();
        }

        return INSTANCE;
    }

    public boolean containsNinja(Player player) {
        boolean result = false;

        for (Ninja np : INSTANCE.ninjaPlayers) {
            if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                result = true;
            }
        }

        return result;
    }

    public void addNinjaPlayer(Ninja ninja) {
        if (!containsNinja(ninja.getPlayer())) {
            INSTANCE.ninjaPlayers.add(ninja);
        }
    }

    public Ninja getNinjaPlayer(Player player) {
        Ninja result = null;

        if (containsNinja(player)) {
            for (Ninja np : INSTANCE.ninjaPlayers) {
                if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    result = np;
                }

            }
        }

        return result;
    }

    public void updateNinjaPlayer(Ninja ninja) {
        if (!containsNinja(ninja.getPlayer())) {
            return;
        }

        Ninja oldNinja = getNinjaPlayer(ninja.getPlayer());
        oldNinja.setClimbing(ninja.isClimbing());
        oldNinja.setTeam(ninja.getTeam());
        NinjaOniAPI.getInstance().getGame().addEntry(ninja.getPlayer(), ninja.getTeam());
    }

    public int countNinja(Game.Teams team) {
        int result = 0;
        for (Ninja ninja : INSTANCE.ninjaPlayers) {
            if (ninja.getTeam() == team) {
                result++;
            }
        }

        return result;
    }

}
