package com.github.hirotask.ninjaoni.runnable;

import com.github.hirotask.ninjaoni.Game;
import com.github.hirotask.ninjaoni.Ninja;
import com.github.hirotask.ninjaoni.NinjaOni;
import com.github.hirotask.ninjaoni.inventory.ItemManager;
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

    private com.github.hirotask.ninjaoni.NinjaOni plugin;
    private BossBar bar;

    private final int moneyAmount;

    public GameTask(NinjaOni ninjaOni, int count) {
        this.plugin = ninjaOni;

        if (count > 0) {
            this.count = count;
            this.MAX_COUNT = count;
        } else {
            this.count = this.plugin.getMyConfig().getGameTime();
            this.MAX_COUNT = this.plugin.getMyConfig().getGameTime();
        }

        this.moneyAmount = this.plugin.getMyConfig().getMoneyAmount();

        this.bar = Bukkit.getServer().createBossBar("残り時間:" + this.MAX_COUNT, BarColor.BLUE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
    }

    @Override
    public void run() {
        if (this.plugin.getGame().getGameState() == Game.GameState.INGAME) {
            int oniCount = this.plugin.getNinjaManager().countNinja(com.github.hirotask.ninjaoni.Game.Teams.ONI);
            int playerCount = this.plugin.getNinjaManager().countNinja(com.github.hirotask.ninjaoni.Game.Teams.PLAYER);
            int lockedCount = this.plugin.getNinjaManager().countNinja(com.github.hirotask.ninjaoni.Game.Teams.LOCKEDPLAYER);

            if(count != MAX_COUNT && count % PACKAGE_TIME == 0) {

                List<Location> locList = this.plugin.getGame().getBorderLocs();

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
                    stand.setHelmet(ItemManager.getMoney());
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

                this.plugin.getGame().gameEnd();
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
                    if (this.plugin.getNinjaManager().getNinjaPlayer(player) != null) {
                        Ninja ninja = this.plugin.getNinjaManager().getNinjaPlayer(player);

                        //表示処理
                        bar.addPlayer(player);
                        bar.setVisible(true);
                        bar.setProgress((float) count / MAX_COUNT);
                        bar.setTitle("残り時間: " + count);

                        StringBuilder sb = new StringBuilder();
                        if(ninja.getTeam() == Game.Teams.PLAYER) {
                            sb.append("残り逃走者: ");
                            sb.append(playerCount);
                            sb.append(" | ");
                            sb.append("残りHP: ");
                            sb.append(ninja.getHp());
                        }else if(ninja.getTeam() == Game.Teams.ONI) {
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
                        if(ninja.getTeam() == Game.Teams.PLAYER) {
                            for(Entity entity : ninja.getPlayer().getNearbyEntities(12,12,12)) {
                                if(!(entity instanceof Player)) {
                                    continue;
                                }

                                Player p = (Player) entity;
                                if (this.plugin.getNinjaManager().getNinjaPlayer(p) != null) {
                                    Ninja nin = this.plugin.getNinjaManager().getNinjaPlayer(p);

                                    if(nin.getTeam() == Game.Teams.ONI) {
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