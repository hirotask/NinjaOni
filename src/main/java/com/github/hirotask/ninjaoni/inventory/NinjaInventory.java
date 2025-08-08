package com.github.hirotask.ninjaoni.inventory;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class NinjaInventory {

    private PlayerInventory inv;

    public NinjaInventory(PlayerInventory inv) {
        this.inv = inv;
    }

    public ItemStack[] getHolderItems() {
        ItemStack[] items = new ItemStack[9];
        for(int i=0; i < items.length; i++) {
            items[i] = inv.getItem(i);
        }

        return items;
    }

    public void decrementHolderItem(ItemStack item) {
        if(inv.contains(item.getType())) {
            java.util.HashMap<Integer, ? extends ItemStack> indexs = inv.all(item.getType());

            for (int key : indexs.keySet()) {
                if (key >= 0 && key <= 8) {
                    int amount = inv.getItem(key).getAmount();
                    if (amount > 1) {
                        inv.getItem(key).setAmount(inv.getItem(key).getAmount() - 1);
                    } else {
                        inv.remove(inv.getItem(key));
                    }
                }
            }
        }
    }

    public void purchaseItem(com.github.hirotask.ninjaoni.Ninja ninja, ItemStack item) {
        if(ninja.getMoney() > 0) {
            PlayerInventory inv = ninja.getPlayer().getInventory();

            if (inv.contains(com.github.hirotask.ninjaoni.inventory.ItemManager.getMoney().getType())) {
                java.util.HashMap<Integer, ? extends ItemStack> indexs = inv.all(com.github.hirotask.ninjaoni.inventory.ItemManager.getMoney().getType());
                for (int key : indexs.keySet()) {
                    if (key == 18) {
                        int amount = inv.getItem(key).getAmount();
                        if (amount > 1) {
                            inv.getItem(key).setAmount(inv.getItem(key).getAmount() - 1);
                        } else {
                            inv.remove(inv.getItem(key));
                        }
                    }

                }

            }

            ninja.getPlayer().playSound(ninja.getPlayer().getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.3F, 1);
            ninja.decMoney();
        }

    }

}
