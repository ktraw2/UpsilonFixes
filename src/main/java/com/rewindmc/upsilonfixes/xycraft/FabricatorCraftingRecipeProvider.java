package com.rewindmc.upsilonfixes.xycraft;

import logisticspipes.proxy.interfaces.ICraftingRecipeProvider;
import logisticspipes.utils.ItemIdentifier;
import logisticspipes.utils.SimpleInventory;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import soaryn.xycraft.machines.block.TileFabricator;

public class FabricatorCraftingRecipeProvider implements ICraftingRecipeProvider {

	@Override
	public boolean canOpenGui(TileEntity tile) {
		System.out.println("Can open "+tile+"?");
		return tile instanceof TileFabricator;
	}

	@Override
	public boolean importRecipe(TileEntity tile, SimpleInventory sinventory) {
		System.out.println("Import recipe from "+tile+" into "+sinventory);
		IInventory inventory = sinventory;
		if (!(tile instanceof TileFabricator)) {
			return false;
		} else {
			TileFabricator fab = (TileFabricator) tile;
			InventoryCrafting matrix = fab.getCraftMatrix();
			System.out.println("Craft matrix "+matrix);
			if (matrix == null) return false;
			fab.loadRecipe();
			System.out.println("Matching recipe "+fab.matchingRecipe);
			if (fab.matchingRecipe == null) return false;
			ItemStack result = fab.matchingRecipe.getRecipeOutput();
			System.out.println("Result "+result);
			if (result == null) return false;
			inventory.setInventorySlotContents(9, result);

			int i;
			ItemStack stackInSlot;
			for (i = 0; i < matrix.getSizeInventory() && i < inventory.getSizeInventory() - 1; ++i) {
				stackInSlot = matrix.getStackInSlot(i) == null ? null : matrix.getStackInSlot(i).copy();
				if (stackInSlot != null && stackInSlot.stackSize > 1) {
					stackInSlot.stackSize = 1;
				}

				System.out.println("Set "+i+" to "+stackInSlot);
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
							System.out.println("Set "+j+" to null");
							inventory.setInventorySlotContents(j, null);
						}
					}
				}
			}

			for (i = 0; i < inventory.getSizeInventory() - 1; ++i) {
				if (inventory.getStackInSlot(i) == null) {
					for (int j = i + 1; j < inventory.getSizeInventory() - 1; ++j) {
						if (inventory.getStackInSlot(j) != null) {
							System.out.println("Set "+i+" to "+inventory.getStackInSlot(j));
							inventory.setInventorySlotContents(i, inventory.getStackInSlot(j));
							System.out.println("Set "+j+" to null");
							inventory.setInventorySlotContents(j, null);
							break;
						}
					}
				}
			}

			System.out.println("Done :)");
			return true;
		}
	}

}