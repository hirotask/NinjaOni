package com.github.hirotask.ninjaoni;

import org.bukkit.entity.Player;

public enum NinjaManager {

    INSTANCE;

    public java.util.List<Ninja> ninjaPlayers;

    public static com.github.hirotask.ninjaoni.NinjaManager getInstance() {
        if(INSTANCE.ninjaPlayers == null) {
            INSTANCE.ninjaPlayers = new java.util.ArrayList<>();
        }

        return INSTANCE;
    }

    public boolean containsNinja(Player player) {
        boolean result = false;

        for (com.github.hirotask.ninjaoni.Ninja np : INSTANCE.ninjaPlayers) {
            if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                result = true;
            }
        }

        return result;
    }

    public void addNinjaPlayer(com.github.hirotask.ninjaoni.Ninja ninja) {
        if (!containsNinja(ninja.getPlayer())) {
            INSTANCE.ninjaPlayers.add(ninja);
        }
    }

    public com.github.hirotask.ninjaoni.Ninja getNinjaPlayer(Player player) {
        com.github.hirotask.ninjaoni.Ninja result = null;

        if (containsNinja(player)) {
            for (com.github.hirotask.ninjaoni.Ninja np : INSTANCE.ninjaPlayers) {
                if (np.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    result = np;
                }

            }
        }

        return result;
    }

    public void updateNinjaPlayer(com.github.hirotask.ninjaoni.Ninja ninja) {
        if (!containsNinja(ninja.getPlayer())) {
            return;
        }

        com.github.hirotask.ninjaoni.Ninja oldNinja = getNinjaPlayer(ninja.getPlayer());
        oldNinja.setClimbing(ninja.isClimbing());
        oldNinja.setTeam(ninja.getTeam());
        com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getGame().addEntry(ninja.getPlayer(), ninja.getTeam());
    }

    public int countNinja(com.github.hirotask.ninjaoni.Game.Teams team) {
        int result = 0;
        for (com.github.hirotask.ninjaoni.Ninja ninja : INSTANCE.ninjaPlayers) {
            if (ninja.getTeam() == team) {
                result++;
            }
        }

        return result;
    }

}
