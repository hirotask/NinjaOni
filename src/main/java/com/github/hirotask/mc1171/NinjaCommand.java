package com.github.hirotask.mc1171;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
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

@Command("ninja")
public class NinjaCommand {

    private static final String[] helpMsgs = {
            "===================",
            "HELP",
            "==================="
    };

    @Default
    public static void ninja(CommandSender sender) {
        for (String msg : helpMsgs) {
            sender.sendMessage(msg);
        }
    }

    @Subcommand("start")
    public static void start(
            Player player,
            @AIntegerArgument int gameTime) {

        com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getGame().gameStart(5, gameTime);
    }

    @Subcommand("getitem")
    public static void getItem(
            Player player,
            @AMultiLiteralArgument({
                    "クナイ",
                    "隠れ玉",
                    "煙玉",
                    "影追玉",
                    "粘着玉",
                    "縮地"
            }) String name) {

        switch (name) {
            case "クナイ" -> {
                player.sendMessage("クナイをインベントリに追加しました");
                player.getInventory().addItem(com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getItemManager().getItem(new com.github.hirotask.mc1171.inventory.item.items.Kunai()));
            }
            case "隠れ玉" -> {
                player.sendMessage("隠れ玉をインベントリに追加しました");
                player.getInventory().addItem(com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getItemManager().getItem(new com.github.hirotask.mc1171.inventory.item.items.Kakure()));
            }
            case "煙玉" -> {
                player.sendMessage("煙玉をインベントリに追加しました");
                player.getInventory().addItem(com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getItemManager().getItem(new com.github.hirotask.mc1171.inventory.item.items.Kemuri()));
            }
            case "影追玉" -> {
                player.sendMessage("影追玉をインベントリに追加しました");
                player.getInventory().addItem(com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getItemManager().getItem(new com.github.hirotask.mc1171.inventory.item.items.Kageoi()));
            }
            case "粘着玉" -> {
                player.sendMessage("粘着玉をインベントリに追加しました");
                player.getInventory().addItem(com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getItemManager().getItem(new com.github.hirotask.mc1171.inventory.item.items.Nenchaku()));
            }
            case "縮地" -> {
                player.sendMessage("縮地をインベントリに追加しました");
                player.getInventory().addItem(com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getItemManager().getItem(new com.github.hirotask.mc1171.inventory.item.items.Shukuchi()));
            }
        }
    }

    @Subcommand("removemoney")
    public static void removeMoney(Player player) {
        World world = com.github.hirotask.mc1171.NinjaManager.getInstance().ninjaPlayers.get(0).getPlayer().getWorld();
        for(Entity entity : world.getEntities()) {
            if(entity instanceof ArmorStand) {
                ArmorStand stand = (ArmorStand) entity;
                if(stand.getCustomName() != null) {
                    if(stand.getCustomName().equals("money")) {
                        stand.remove();
                    }
                }
            }
        }
        player.sendMessage("お金をすべて削除しました");
    }

    @Subcommand("spawnshop")
    public static void spawnShop(Player player,
                                 @AMultiLiteralArgument({"鬼", "プレイヤー"}) String type) {
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

        if(type.equalsIgnoreCase("鬼")) {
            villager.setCustomName(ChatColor.GREEN + "鬼専用ショップ");
        } else if (type.equalsIgnoreCase("プレイヤー")){
            villager.setCustomName(ChatColor.GREEN + "プレイヤー専用ショップ");
        }
    }

    @Subcommand("warp")
    public static void warp(Player player) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            Bukkit.getServer().getLogger().info(p.getName());
            if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getMyConfig().getWarpBlockTypeOni()) {
                com.github.hirotask.mc1171.NinjaManager.getInstance().updateNinjaPlayer(new com.github.hirotask.mc1171.Ninja(p, com.github.hirotask.mc1171.Game.Teams.ONI));
                p.sendMessage("あなたは鬼になりました");
            } else if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == com.github.hirotask.mc1171.NinjaOniAPI.getInstance().getMyConfig().getWarpBlockTypeSpec()) {
                com.github.hirotask.mc1171.NinjaManager.getInstance().updateNinjaPlayer(new com.github.hirotask.mc1171.Ninja(p, com.github.hirotask.mc1171.Game.Teams.SPECTATOR));
                p.sendMessage("あなたは観戦者になりました");
            }
        }
    }
}
