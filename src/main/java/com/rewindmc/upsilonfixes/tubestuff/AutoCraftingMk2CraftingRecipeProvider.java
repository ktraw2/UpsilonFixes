package com.rewindmc.upsilonfixes.tubestuff;

import com.rewindmc.upsilonfixes.logisticspipes.UpsilonCraftingRecipeProvider;
import immibis.tubestuff.TileAutoCraftingMk2;
import logisticspipes.utils.SimpleInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;

public class AutoCraftingMk2CraftingRecipeProvider extends UpsilonCraftingRecipeProvider {
    @Override
    public boolean canOpenGui(TileEntity tileEntity) {
        return tileEntity instanceof TileAutoCraftingMk2;
    }

    @Override
    public boolean importRecipe(TileEntity tileEntity, SimpleInventory simpleInventory) {
        IInventory inventory = simpleInventory;
        if (!(tileEntity instanceof TileAutoCraftingMk2))
            return false;

        TileAutoCraftingMk2 crafterMk2 = (TileAutoCraftingMk2) tileEntity;
        ItemStack[][] inputMatrix = crafterMk2.recipeInputs;
        IRecipe recipe = crafterMk2.cachedRecipe;
        if (inputMatrix == null || recipe == null)
            return false;
        ItemStack result = crafterMk2.cachedRecipe.getRecipeOutput();
        if (result == null)
            return false;

        inventory.setInventorySlotContents(9, result.copy());

        for (int i=0; i < inputMatrix.length; i++) {
            ItemStack stackInSlot = null;
            // Table can have multiple ItemStacks for the same slot if oredictionary mode is enabled
            ItemStack[] possibleInputs = inputMatrix[i];
            if (possibleInputs.length > 0) {
                stackInSlot = possibleInputs[0].copy();
                stackInSlot.count = 1;
            }
            inventory.setInventorySlotContents(i, stackInSlot);
        }

        this.mergeStacks(inventory);

        return true;
    }
}
