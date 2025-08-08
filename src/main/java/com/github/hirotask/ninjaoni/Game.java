package com.github.hirotask.ninjaoni;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.List;

import static com.github.hirotask.ninjaoni.Game.Teams.*;
import static com.github.hirotask.ninjaoni.Game.Teams.SPECTATOR;

/* ゲームにまつわる処理の根幹を担うクラス*/
public class Game {


    /*
    フィールド
     */
    @Getter
    @Setter
    private GameState gameState;

    @Getter
    private final List<Location> borderLocs = new java.util.ArrayList<>();

    //スコアボード
    private ScoreboardManager sm;
    private Scoreboard board;
    private static Team oni;
    private static Team pl;
    private static Team lockedpl;
    private static Team spectator;

    /**
     * ゲームのセットアップを行うメソッド
     */
    public void setup() {
        this.gameState = com.github.hirotask.ninjaoni.Game.GameState.NONE;


        initTeams();
    }

    /**
     * スコアボードTeamのセットアップを行うメソッド
     */
    private void initTeams() {
        sm = Bukkit.getScoreboardManager();
        board = sm.getMainScoreboard();

        if (board.getTeam(ONI.getTeamName()) == null) {
            oni = board.registerNewTeam(ONI.getTeamName());
        } else {
            oni = board.getTeam(ONI.getTeamName());
        }
        oni.setPrefix(ONI.getPrefix());
        oni.setSuffix(ONI.getSuffix());
        oni.setDisplayName(ONI.getTeamName());
        oni.setAllowFriendlyFire(false);
        oni.setColor(ChatColor.DARK_AQUA);
        oni.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        if (board.getTeam(PLAYER.getTeamName()) == null) {
            pl = board.registerNewTeam(PLAYER.getTeamName());
        } else {
            pl = board.getTeam(PLAYER.getTeamName());
        }
        pl.setPrefix(PLAYER.getPrefix());
        pl.setSuffix(PLAYER.getSuffix());
        pl.setDisplayName(PLAYER.getTeamName());
        pl.setAllowFriendlyFire(false);
        pl.setColor(ChatColor.WHITE);
        pl.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        if(board.getTeam(LOCKEDPLAYER.getTeamName()) == null) {
            lockedpl = board.registerNewTeam(LOCKEDPLAYER.getTeamName());
        } else {
            lockedpl = board.getTeam(LOCKEDPLAYER.getTeamName());
        }
        lockedpl.setPrefix(LOCKEDPLAYER.getPrefix());
        lockedpl.setSuffix(LOCKEDPLAYER.getSuffix());
        lockedpl.setDisplayName(LOCKEDPLAYER.getTeamName());
        lockedpl.setAllowFriendlyFire(false);
        lockedpl.setColor(ChatColor.RED);

        if (board.getTeam(SPECTATOR.getTeamName()) == null) {
            spectator = board.registerNewTeam(SPECTATOR.getTeamName());
        } else {
            spectator = board.getTeam(SPECTATOR.getTeamName());
        }
        spectator.setPrefix(SPECTATOR.getPrefix());
        spectator.setSuffix(SPECTATOR.getSuffix());
        spectator.setDisplayName(SPECTATOR.getTeamName());
        spectator.setAllowFriendlyFire(false);
        spectator.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            pl.addEntry(player.getName());
            lockedpl.removeEntry(player.getName());
            spectator.removeEntry(player.getName());
            oni.removeEntry(player.getName());
        }
    }

    public void addEntry(Player player, com.github.hirotask.ninjaoni.Game.Teams team) {
        switch (team) {
            case ONI -> oni.addEntry(player.getName());
            case SPECTATOR -> spectator.addEntry(player.getName());
            case LOCKEDPLAYER -> lockedpl.addEntry(player.getName());
            default -> pl.addEntry(player.getName());
        }
    }

    /**
     * ゲーム終了時の処理
     */
    public void gameEnd() {
        //チームから全員外す
        for (String name : oni.getEntries()) {
            oni.removeEntry(name);
        }
        for (String name : pl.getEntries()) {
            pl.removeEntry(name);
        }
        for (String name : spectator.getEntries()) {
            spectator.removeEntry(name);
        }

        //ゲーム状態変更
        this.gameState = com.github.hirotask.ninjaoni.Game.GameState.NONE;

        //全員のゲームモード変更
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
        }

        for (com.github.hirotask.ninjaoni.Ninja np : com.github.hirotask.ninjaoni.NinjaManager.getInstance().ninjaPlayers) {
            np.setHp(60);
            np.setMoney(0);
            np.setLocked(false);
            np.setTeam(PLAYER);
            np.setItems(new java.util.ArrayList<>());
        }

        //インベントリ初期化
        for(com.github.hirotask.ninjaoni.Ninja ninja : com.github.hirotask.ninjaoni.NinjaManager.getInstance().ninjaPlayers) {
            ninja.setMoney(0);
            ninja.getPlayer().getInventory().clear(); //インベントリ初期化
        }
    }

    /**
     * ゲームスタート時の処理
     * @param countdownTime
     * @param gameTime
     */
    public void gameStart(int countdownTime, int gameTime) {
        gameState = com.github.hirotask.ninjaoni.Game.GameState.COUNTDOWN;

        //チームの設定
        for (com.github.hirotask.ninjaoni.Ninja ninja : com.github.hirotask.ninjaoni.NinjaManager.getInstance().ninjaPlayers) {
            if (ninja.getTeam() == SPECTATOR) {
                //観戦者チームにいた場合
                addEntry(ninja.getPlayer(), SPECTATOR);
                ninja.getPlayer().setGameMode(GameMode.SPECTATOR);
            } else if (ninja.getTeam() == ONI) {
                //鬼チームにいた場合
                Player player = ninja.getPlayer();
                addEntry(ninja.getPlayer(), ONI);
                Location loc = com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getMyConfig().getTPLocationOni().clone();
                loc.setWorld(player.getWorld());
                player.teleport(loc);
            } else {
                //プレイヤーの場合
                ninja.setTeam(com.github.hirotask.ninjaoni.Game.Teams.PLAYER);
                Player player = ninja.getPlayer();
                addEntry(ninja.getPlayer(), PLAYER);
                Location loc = com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getMyConfig().getTPLocationPlayer().clone();
                loc.setWorld(player.getWorld());
                player.teleport(loc);
            }
        }

        //インベントリの設定
        for(com.github.hirotask.ninjaoni.Ninja ninja : com.github.hirotask.ninjaoni.NinjaManager.getInstance().ninjaPlayers) {
            ninja.setMoney(0);
            ninja.getPlayer().getInventory().clear(); //インベントリ初期化

            PlayerInventory inv = ninja.getPlayer().getInventory();

            int slot = 20;
            for(com.github.hirotask.ninjaoni.inventory.item.NinjaItem ninjaItem : ninja.getItems()) {
                ItemStack item = com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getItemManager().getItem(ninjaItem);
                item.setAmount(64);
                inv.setItem(slot, item);
                slot++;
            }

            inv.setItem(9, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
            inv.setItem(27, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));


            if(ninja.getTeam() == ONI){
                inv.setHelmet(com.github.hirotask.ninjaoni.inventory.ItemManager.getOniHelmet());
                inv.setChestplate(com.github.hirotask.ninjaoni.inventory.ItemManager.getOniChestplate());
                inv.setLeggings(com.github.hirotask.ninjaoni.inventory.ItemManager.getOniLeggings());
                inv.setBoots(com.github.hirotask.ninjaoni.inventory.ItemManager.getOniBoots());
            }
        }

        //お金の消去
        World world = com.github.hirotask.ninjaoni.NinjaManager.getInstance().ninjaPlayers.get(0).getPlayer().getWorld();
        for(Entity entity : world.getEntities()) {
            if(entity instanceof ArmorStand stand) {
                if(stand.getCustomName() != null) {
                    if(stand.getCustomName().equals("money")) {
                        stand.remove();
                    }
                }
            }
        }

        //WorldBorderの設定
        //中心座標の取得
        WorldBorder border = world.getWorldBorder();
        Location center = border.getCenter();

        double x = center.getX() - (border.getSize() / 2);
        double z = center.getZ() - (border.getSize() / 2);


        for(double dx=0; dx < border.getSize(); dx += 5) {
            for(double dz=0; dz < border.getSize(); dz += 5) {
                borderLocs.add(new Location(world,x + dx, 150.0, z + dz));
            }
        }

        //Taskの実行
        new com.github.hirotask.ninjaoni.runnable.CountDownTask(countdownTime).runTaskTimer(com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getPlugin(), 0L, 20L);
        new com.github.hirotask.ninjaoni.runnable.GameTask(gameTime).runTaskTimer(com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getPlugin(), 0L, 20L);
        new com.github.hirotask.ninjaoni.runnable.MovementTask().runTaskTimer(com.github.hirotask.ninjaoni.NinjaOniAPI.getInstance().getPlugin(), 0L, 20L);
    }



    public enum GameState {
        INGAME, //ゲーム中
        COUNTDOWN, //カウントダウン中
        NONE //何もしていない
    }

    public enum Teams {
        ONI("oni","§c", "§f"), //鬼チーム
        PLAYER("player", "§9", "§f"), //プレイヤーチーム
        LOCKEDPLAYER("lockedplayer", "",""), //捕まっているプレイヤーチーム
        SPECTATOR("spectator", "§7", "§f"); //観戦者チーム

        private String teamName;
        private String prefix;
        private String suffix;

        Teams(String teamName, String prefix, String suffix) {
            this.teamName = teamName;
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public String getTeamName() {
            return teamName;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getSuffix() {
            return suffix;
        }
    }
}
