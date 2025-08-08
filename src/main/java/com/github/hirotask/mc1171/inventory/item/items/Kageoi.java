package com.github.hirotask.mc1171.inventory.item.items;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.github.hirotask.mc1171.inventory.item.NinjaItem;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Kageoi implements com.github.hirotask.mc1171.inventory.item.NinjaItem {

    @Override
    public Material type() {
        return Material.HEART_OF_THE_SEA;
    }

    @Override
    public java.util.HashMap<Enchantment, Integer> enchants() {
        return null;
    }

    @Override
    public String name() {
        return "影追玉";
    }

    @Override
    public void execute(com.github.hirotask.mc1171.Ninja ninja) {
        Player player = ninja.getPlayer();

        java.util.List<Player> glowPlayers = new java.util.ArrayList<>();

        //光らせるプレイヤーの設定
        for (com.github.hirotask.mc1171.Ninja nin : com.github.hirotask.mc1171.NinjaManager.getInstance().ninjaPlayers) {
            if (nin.getTeam() == com.github.hirotask.mc1171.Game.Teams.PLAYER) {
                if(!glowPlayers.contains(nin.getPlayer())) {
                    glowPlayers.add(nin.getPlayer());
                }
            }
        }

        new BukkitRunnable() {

            int count = 8;

            @Override
            public void run() {
                if(count < 0) {
                    this.cancel();
                } else {
                    for (Player p : glowPlayers) {

                        PacketContainer glowPacket = com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getProtocol().createPacket(PacketType.Play.Server.ENTITY_METADATA);
                        glowPacket.getIntegers().write(0, p.getEntityId()); //光らせるプレイヤーのID
                        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
                        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
                        WrappedDataWatcher.Serializer serializer2 = WrappedDataWatcher.Registry.get(Integer.class);
                        watcher.setEntity(p); //光らせるプレイヤーを指定
                        watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page

                        glowPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created

                        try {
                            com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getProtocol().sendServerPacket(player, glowPacket);
                        } catch (java.lang.reflect.InvocationTargetException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                count--;
            }

        }.runTaskTimer(com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getPlugin(), 0L, 20L);
    }

    @Override
    public com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType ninjaItemType() {
        return com.github.hirotask.mc1171.inventory.item.NinjaItem.NinjaItemType.ONI_ITEM;
    }
}
