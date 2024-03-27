package com.rewindmc.upsilonfixes.xycraft;

import com.rewindmc.upsilonfixes.logisticspipes.UpsilonCraftingRecipeProvider;
import logisticspipes.utils.SimpleInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import soaryn.xycraft.machines.block.TileFabricator;

public class FabricatorCraftingRecipeProvider extends UpsilonCraftingRecipeProvider {

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
				if (stackInSlot != null && stackInSlot.count > 1) {
					stackInSlot.count = 1;
				}

				inventory.setInventorySlotContents(i, stackInSlot);
			}

			this.mergeStacks(sinventory);

			return true;
		}
	}

}