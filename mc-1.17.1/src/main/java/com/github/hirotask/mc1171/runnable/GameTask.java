package com.github.hirotask.mc1171.runnable;

import java.util.Collections;
import java.util.List;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final int PACKAGE_TIME = 30;
    private final int PACKAGE_RANGE = 10;

    private int count;
    private final int MAX_COUNT;

    private com.github.hirotask.mc1171.NinjaOni2 plugin;
    private BossBar bar;

    private final int moneyAmount;

    public GameTask(int count) {
        this.plugin = com.github.hirotask.mc1171.NinjaOniAPI.INSTANCE.getPlugin();

        if (count > 0) {
            this.count = count;
            this.MAX_COUNT = count;
        } else {
            this.count = com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getMyConfig().getGameTime();
            this.MAX_COUNT = com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getMyConfig().getGameTime();
        }

        this.moneyAmount = com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getMyConfig().getMoneyAmount();

        this.bar = Bukkit.getServer().createBossBar("残り時間:" + this.MAX_COUNT, BarColor.BLUE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
    }

    @Override
    public void run() {
        if (com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getGame().getGameState() == com.github.hirotask.mc1171.Game.GameState.INGAME) {
            int oniCount = com.github.hirotask.mc1171.NinjaManager.getInstance().countNinja(com.github.hirotask.mc1171.Game.Teams.ONI);
            int playerCount = com.github.hirotask.mc1171.NinjaManager.getInstance().countNinja(com.github.hirotask.mc1171.Game.Teams.PLAYER);
            int lockedCount = com.github.hirotask.mc1171.NinjaManager.getInstance().countNinja(com.github.hirotask.mc1171.Game.Teams.LOCKEDPLAYER);

            if(count != MAX_COUNT && count % PACKAGE_TIME == 0) {


                List<Location> locList = com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getGame().getBorderLocs();

                Collections.shuffle(locList);

                for(int i=0; i < moneyAmount; i++) {
                    Location loc = locList.get(i);

                    //アーマースタンド出現
                    Entity entity = loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                    ArmorStand stand = (ArmorStand) entity;
                    stand.setBasePlate(false);
                    stand.setInvisible(true);
                    stand.setSmall(true);
                    stand.setCollidable(false);
                    stand.setCanPickupItems(false);
                    stand.setInvulnerable(true);
                    stand.setCustomName("money");
                    stand.setHelmet(com.github.hirotask.mc1171.inventory.ItemManager.getMoney());
                }

            }

            if (count == 0 || oniCount == 0 || playerCount == 0 || lockedCount == playerCount) {
                String subTitle = "";

                if(oniCount > playerCount || lockedCount == playerCount) {
                    subTitle = "鬼の勝利！";
                } else if(oniCount < playerCount) {
                    subTitle = "プレイヤーの勝利";
                } else {
                    subTitle = "引き分け！";
                }

                com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getGame().gameEnd();
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle("GAME OVER!", subTitle, 10, 70, 2);
                }

                for(Player player : Bukkit.getOnlinePlayers()) {
                    bar.removePlayer(player);
                }
                bar.removeAll();

                this.cancel();
            } else {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (com.github.hirotask.mc1171.NinjaManager.getInstance().getNinjaPlayer(player) != null) {
                        com.github.hirotask.mc1171.Ninja ninja = com.github.hirotask.mc1171.NinjaManager.getInstance().getNinjaPlayer(player);

                        //表示処理
                        bar.addPlayer(player);
                        bar.setVisible(true);
                        bar.setProgress((float) count / MAX_COUNT);
                        bar.setTitle("残り時間: " + count);

                        StringBuilder sb = new StringBuilder();
                        if(ninja.getTeam() == com.github.hirotask.mc1171.Game.Teams.PLAYER) {
                            sb.append("残り逃走者: ");
                            sb.append(playerCount);
                            sb.append(" | ");
                            sb.append("残りHP: ");
                            sb.append(ninja.getHp());
                        }else if(ninja.getTeam() == com.github.hirotask.mc1171.Game.Teams.ONI) {
                            sb.append("残り逃走者: ");
                            sb.append(playerCount);
                            sb.append(" | ");
                            sb.append("残り鬼： ");
                            sb.append(oniCount);
                        } else {
                            sb.append("残り逃走者: ");
                            sb.append(playerCount);
                            sb.append(" | ");
                            sb.append("残りHP: ");
                            sb.append(ninja.getHp());
                            sb.append(" | ");
                            sb.append("残り鬼： ");
                            sb.append(oniCount);
                        }


                        TextComponent component = new TextComponent();
                        component.setText(sb.toString());

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);

                        //鬼が近づいてきたときの処理
                        if(ninja.getTeam() == com.github.hirotask.mc1171.Game.Teams.PLAYER) {
                            for(Entity entity : ninja.getPlayer().getNearbyEntities(12,12,12)) {
                                if(!(entity instanceof Player)) {
                                    continue;
                                }

                                Player p = (Player) entity;
                                if (com.github.hirotask.mc1171.NinjaManager.getInstance().getNinjaPlayer(p) != null) {
                                    com.github.hirotask.mc1171.Ninja nin = com.github.hirotask.mc1171.NinjaManager.getInstance().getNinjaPlayer(p);

                                    if(nin.getTeam() == com.github.hirotask.mc1171.Game.Teams.ONI) {
                                        ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 0.5f);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            count--;
        }
    }
}