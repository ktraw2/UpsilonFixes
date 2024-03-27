package com.rewindmc.upsilonfixes.logisticspipes;

import logisticspipes.proxy.interfaces.ICraftingRecipeProvider;
import logisticspipes.utils.ItemIdentifier;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class UpsilonCraftingRecipeProvider implements ICraftingRecipeProvider {
    protected void mergeStacks(IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory() - 1; ++i) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (stackInSlot != null) {
                ItemIdentifier itemInSlot = ItemIdentifier.get(stackInSlot);

                for (int j = i + 1; j < inventory.getSizeInventory() - 1; ++j) {
                    ItemStack stackInOtherSlot = inventory.getStackInSlot(j);
                    if (stackInOtherSlot != null && itemInSlot == ItemIdentifier.get(stackInOtherSlot)) {
                        stackInSlot.count += stackInOtherSlot.count;
                        inventory.setInventorySlotContents(j, null);
                    }
                }
            }
        }

        for (int i = 0; i < inventory.getSizeInventory() - 1; ++i) {
            if (inventory.getStackInSlot(i) == null) {
                for (int j = i + 1; j < inventory.getSizeInventory() - 1; ++j) {
                    if (inventory.getStackInSlot(j) != null) {
                        inventory.setInventorySlotContents(i, inventory.getStackInSlot(j));
                        inventory.setInventorySlotContents(j, null);
                        break;
                    }
                }
            }
        }
    }
}
