package jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.runnable;

import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.*;
import jp.ne.sakura.erudoblog.ninjaoni2.ninjaoni2.inventory.ItemManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

public class GameTask extends BukkitRunnable {

    private final int PACKAGE_TIME = 30;
    private final int PACKAGE_RANGE = 10;

    private int count;
    private final int MAX_COUNT;

    private NinjaOni2 plugin;
    private BossBar bar;

    private final int moneyAmount;

    public GameTask(int count) {
        this.plugin = NinjaOniAPI.INSTANCE.getPlugin();

        if (count > 0) {
            this.count = count;
            this.MAX_COUNT = count;
        } else {
            this.count = NinjaOniAPI.getInstance().getMyConfig().getGameTime();
            this.MAX_COUNT = NinjaOniAPI.getInstance().getMyConfig().getGameTime();
        }

        this.moneyAmount = NinjaOniAPI.getInstance().getMyConfig().getMoneyAmount();

        this.bar = Bukkit.getServer().createBossBar("残り時間:" + this.MAX_COUNT, BarColor.BLUE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
    }

    @Override
    public void run() {
        if (NinjaOniAPI.getInstance().getGame().getGameState() == Game.GameState.INGAME) {
            int oniCount = NinjaManager.getInstance().countNinja(Game.Teams.ONI);
            int playerCount = NinjaManager.getInstance().countNinja(Game.Teams.PLAYER);
            int lockedCount = NinjaManager.getInstance().countNinja(Game.Teams.LOCKEDPLAYER);

            if(count != MAX_COUNT && count % PACKAGE_TIME == 0) {


                List<Location> locList = NinjaOniAPI.getInstance().getGame().getBorderLocs();

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

                NinjaOniAPI.getInstance().getGame().gameEnd();
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
                    if (NinjaManager.getInstance().getNinjaPlayer(player) != null) {
                        Ninja ninja = NinjaManager.getInstance().getNinjaPlayer(player);

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
                                if (NinjaManager.getInstance().getNinjaPlayer(p) != null) {
                                    Ninja nin = NinjaManager.getInstance().getNinjaPlayer(p);

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