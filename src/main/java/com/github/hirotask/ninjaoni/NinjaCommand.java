package com.github.hirotask.ninjaoni;

import com.github.hirotask.ninjaoni.inventory.ItemManager;
import com.github.hirotask.ninjaoni.inventory.item.NinjaItem;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class NinjaCommand {

    private final NinjaOni ninjaOni;

    public NinjaCommand(NinjaOni ninjaOni) {
        this.ninjaOni = ninjaOni;
    }

    private static final String[] helpMsgs = {
            "===================",
            "HELP",
            "==================="
    };

    /**
     * 取得できるアイテム一覧
     */
    private String[] getCanGetItems() {
        return ninjaOni.getItemManager().getNinjaItems().stream().map(NinjaItem::name).toArray(String[]::new);
    }

    private void sendHelpMessage(CommandSender sender, CommandArguments ignoredArgs) {
        for (String msg : helpMsgs) {
            sender.sendMessage(msg);
        }
    }

    private void start(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        int gameTime = args.get(0) != null ? (int) args.get(0) : -1;
        int countDownTime = 5;

        if (gameTime > 0) {
            ninjaOni.getGame().gameStart(countDownTime, gameTime);
        } else {
            player.sendMessage("実行中にエラーが発生しました");
        }
    }

    private void getItem(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        String itemName = (String) args.get(0);

        System.out.print(itemName);

        if (itemName == null) return;

        ItemManager itemManager = this.ninjaOni.getItemManager();
        Map<String, NinjaItem> map = itemManager.getNinjaItemMap();
        NinjaItem item = map.get(itemName);

        if (item != null) {
            player.sendMessage(item.name() + "をインベントリに追加しました");
            player.getInventory().addItem(itemManager.getItem(item));
        }
    }

    private void removeMoney(CommandSender sender, CommandArguments ignoredArgs) {
        Player player = (Player) sender;
        World world = player.getWorld();

        for(Entity entity : world.getEntities()) {
            if(entity instanceof ArmorStand stand) {
                if(stand.getCustomName() != null) {
                    if(stand.getCustomName().equals("money")) {
                        stand.remove();
                    }
                }
            }
        }

        player.sendMessage("お金をすべて削除しました");
    }

    private void spawnShop(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        String type = (String) args.get(0);

        Location loc = player.getLocation();

        Villager villager = (Villager) player.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        villager.setAI(false);
        villager.setAdult();
        villager.setCanPickupItems(false);
        villager.setCustomNameVisible(true);
        villager.setGravity(true);
        villager.setSilent(true);
        villager.setInvulnerable(true);
        villager.setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());

        if (type != null) {
            if(type.equalsIgnoreCase("oni")) {
                villager.setCustomName(ChatColor.GREEN + "鬼専用ショップ");
            } else if (type.equalsIgnoreCase("player")){
                villager.setCustomName(ChatColor.GREEN + "プレイヤー専用ショップ");
            }
        }
    }

    private void warp(CommandSender sender, CommandArguments args) {

        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            Bukkit.getServer().getLogger().info(p.getName());
            if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == ninjaOni.getMyConfig().getWarpBlockTypeOni()) {
                ninjaOni.getNinjaManager().updateNinjaPlayer(new Ninja(this.ninjaOni, p, Game.Teams.ONI));
                p.sendMessage("あなたは鬼になりました");
            } else if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == ninjaOni.getMyConfig().getWarpBlockTypeSpec()) {
                ninjaOni.getNinjaManager().updateNinjaPlayer(new Ninja(this.ninjaOni, p, Game.Teams.SPECTATOR));
                p.sendMessage("あなたは観戦者になりました");
            }
        }
    }

    public void registerAllCommands() {
        new CommandTree("ninja")
                .executes(this::sendHelpMessage)
                .then(new LiteralArgument("start")
                        .then(new IntegerArgument("gameTime")
                                .executes(this::start)
                        )
                )
                .then(new LiteralArgument("getItem")
                        .then(new MultiLiteralArgument("name", this.getCanGetItems())
                                .executes(this::getItem)
                        )
                )
                .then(new LiteralArgument("removemoney")
                        .executes(this::removeMoney)
                )
                .then(new LiteralArgument("spawnshop")
                        .then(new MultiLiteralArgument("type", "oni", "player")
                                .executes(this::spawnShop)
                        )
                )
                .then(new LiteralArgument("warp")
                        .executes(this::warp)
                )
                .register();
    }

}
