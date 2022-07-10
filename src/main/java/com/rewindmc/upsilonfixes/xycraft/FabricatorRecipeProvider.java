package com.rewindmc.upsilonfixes.xycraft;

import logisticspipes.utils.ItemIdentifier;
import logisticspipes.utils.SimpleInventory;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import soaryn.xycraft.machines.block.TileFabricator;

public class FabricatorRecipeProvider {

    public static boolean canOpenGui(TileEntity tile) {
        return tile instanceof TileFabricator;
    }

    public static boolean importRecipe(TileEntity tile, SimpleInventory inventory) {
        if (!(tile instanceof TileFabricator)) {
            return false;
        } else {
            TileFabricator fab = (TileFabricator) tile;
            InventoryCrafting inv = fab.getCraftMatrix();
            IRecipe recipe = fab.matchingRecipe;
           // if (recipe == null || inv == null) return false;
            ItemStack result = fab.matchingRecipe.getRecipeOutput();
            //if (result == null) return false;
            inventory.a(9, result);

            int i;
            ItemStack stackInSlot;
            for (i = 0; i < inv.stackList.length && i < inventory.k_() - 1; ++i) {
                stackInSlot = inv.stackList[i] == null ? null : inv.stackList[i].copy();
                if (stackInSlot != null && stackInSlot.stackSize > 1) {
                    stackInSlot.stackSize = 1;
                }

                inventory.a(i, stackInSlot);
            }

            for (i = 0; i < inventory.k_() - 1; ++i) {
                stackInSlot = inventory.a(i);
                if (stackInSlot != null) {
                    ItemIdentifier itemInSlot = ItemIdentifier.get(stackInSlot);

                    for (int j = i + 1; j < inventory.k_() - 1; ++j) {
                        ItemStack stackInOtherSlot = inventory.a(j);
                        if (stackInOtherSlot != null && itemInSlot == ItemIdentifier.get(stackInOtherSlot)) {
                            stackInSlot.stackSize += stackInOtherSlot.stackSize;
                            inventory.a(j, null);
                        }
                    }
                }
            }

            for (i = 0; i < inventory.k_() - 1; ++i) {
                if (inventory.a(i) == null) {
                    for (int j = i + 1; j < inventory.k_() - 1; ++j) {
                        if (inventory.a(j) != null) {
                            inventory.a(i, inventory.a(j));
                            inventory.a(j, null);
                            break;
                        }
                    }
                }
            }

            return true;

        }
    }
}