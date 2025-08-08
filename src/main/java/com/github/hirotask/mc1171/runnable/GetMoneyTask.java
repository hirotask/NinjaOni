package com.github.hirotask.mc1171.runnable;

import com.github.hirotask.mc1171.inventory.ItemManager;
import com.github.hirotask.mc1171.Ninja;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

//お金取得タスク
public class GetMoneyTask extends BukkitRunnable {

    private int count = 0;
    private com.github.hirotask.mc1171.Ninja ninja;
    private ArmorStand stand;

    public GetMoneyTask(com.github.hirotask.mc1171.Ninja ninja, ArmorStand stand, int count) {
        this.ninja = ninja;
        this.count = count * 20;
        this.stand = stand;
    }

    @Override
    public void run() {
        if(count < 0 || !ninja.getPlayer().isSneaking() || ninja.getPlayer().getLocation().distance(stand.getLocation()) >= 3) {
            this.cancel();
            return;
        }


        if (count == 0) {
            Inventory inv = ninja.getPlayer().getInventory();
            if (inv.getItem(18) == null || inv.getItem(18).clone().getAmount() <= 0) {
                inv.setItem(18, com.github.hirotask.mc1171.inventory.ItemManager.getMoney());
            } else {
                int amount = inv.getItem(18).clone().getAmount();
                ItemStack item = com.github.hirotask.mc1171.inventory.ItemManager.getMoney().clone();
                item.setAmount(amount + 1);
                inv.setItem(18, item);
            }

            stand.remove();
            ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1);
            ninja.getPlayer().sendTitle(ChatColor.YELLOW + "取得完了", null, 10, 70, 2);
            ninja.incMoney();
        } else if (count % 20 == 0) {
            String frame = ChatColor.YELLOW + "◆";
            StringBuilder sb = new StringBuilder();
            for (int i = count / 20; i > 0; i--) {
                sb.append(frame);
            }
            sb.append(ChatColor.RED + "取得中");
            for (int i = count / 20; i > 0; i--) {
                sb.append(frame);
            }

            ninja.getPlayer().sendTitle(sb.toString(), null, 10, 70, 2);
        } else {
            ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 10);
        }


        count--;
    }
}
