package com.rewindmc.upsilonfixes.xycraft;

import logisticspipes.proxy.interfaces.ICraftingRecipeProvider;
import logisticspipes.utils.ItemIdentifier;
import logisticspipes.utils.SimpleInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import soaryn.xycraft.machines.block.TileFabricator;

public class FabricatorCraftingRecipeProvider implements ICraftingRecipeProvider {

	@Override
	public boolean canOpenGui(TileEntity tile) {
		return tile instanceof TileFabricator;
	}

	@Override
	public boolean importRecipe(TileEntity tile, SimpleInventory sinventory) {
		IInventory inventory = sinventory;
		if (!(tile instanceof TileFabricator)) {
			return false;
		} else {
			TileFabricator fab = (TileFabricator) tile;
			InventoryCrafting matrix = fab.getCraftMatrix();
			if (matrix == null) return false;
			fab.loadRecipe();
			if (fab.matchingRecipe == null) return false;
			ItemStack result = fab.matchingRecipe.getRecipeOutput();
			if (result == null) return false;
			inventory.setInventorySlotContents(9, result.copy());

			int i;
			ItemStack stackInSlot;
			for (i = 0; i < matrix.getSizeInventory() && i < inventory.getSizeInventory() - 1; ++i) {
				stackInSlot = matrix.getStackInSlot(i) == null ? null : matrix.getStackInSlot(i).copy();
				if (stackInSlot != null && stackInSlot.stackSize > 1) {
					stackInSlot.stackSize = 1;
				}

				inventory.setInventorySlotContents(i, stackInSlot);
			}

			for (i = 0; i < inventory.getSizeInventory() - 1; ++i) {
				stackInSlot = inventory.getStackInSlot(i);
				if (stackInSlot != null) {
					ItemIdentifier itemInSlot = ItemIdentifier.get(stackInSlot);

					for (int j = i + 1; j < inventory.getSizeInventory() - 1; ++j) {
						ItemStack stackInOtherSlot = inventory.getStackInSlot(j);
						if (stackInOtherSlot != null && itemInSlot == ItemIdentifier.get(stackInOtherSlot)) {
							stackInSlot.stackSize += stackInOtherSlot.stackSize;
							inventory.setInventorySlotContents(j, null);
						}
					}
				}
			}

			for (i = 0; i < inventory.getSizeInventory() - 1; ++i) {
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

			return true;
		}
	}

}