package com.github.hirotask.ninjaoni;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class NinjaManager {

    private final NinjaOni ninjaOni;

    public List<Ninja> ninjaPlayers;

    public NinjaManager(NinjaOni ninjaOni, List<Ninja> ninjaPlayers) {
        this.ninjaOni = ninjaOni;
        this.ninjaPlayers = ninjaPlayers;
    }

    public NinjaManager(NinjaOni ninjaOni) {
        this.ninjaOni = ninjaOni;
        this.ninjaPlayers = new ArrayList<>();
    }

    public boolean containsNinja(Player player) {
        boolean result = false;

        for (Ninja np : this.ninjaPlayers) {
            if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                result = true;
            }
        }

        return result;
    }

    public void addNinjaPlayer(Ninja ninja) {
        if (!containsNinja(ninja.getPlayer())) {
            this.ninjaPlayers.add(ninja);
        }
    }

    public Ninja getNinjaPlayer(Player player) {
        Ninja result = null;

        if (containsNinja(player)) {
            for (Ninja np : this.ninjaPlayers) {
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
        this.ninjaOni.getGame().addEntry(ninja.getPlayer(), ninja.getTeam());
    }

    public int countNinja(Game.Teams team) {
        int result = 0;
        for (Ninja ninja : this.ninjaPlayers) {
            if (ninja.getTeam() == team) {
                result++;
            }
        }

        return result;
    }

}
