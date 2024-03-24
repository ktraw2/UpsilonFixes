package com.rewindmc.upsilonfixes.rp2;

import codechicken.nei.FastTransferManger;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.xycraftcopy.core.inventory.InventoryUtils;
import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import net.minecraft.client.gui.handled.HandledScreen;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import nilloader.api.lib.asm.Type;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

import java.util.Arrays;
import java.util.List;

@Patch.Class("codechicken.nei.plugins.redpower.NEIRedPowerConfig")
@ConfigOptions("betterProjectTableNEIOverlay")
public class NEIRedPowerConfigTransformer extends UpsilonMiniTransformer {
    @Patch.Method("loadConfig()V")
    public void patchAdvBenchGuiOverlayHandler(PatchContext ctx) {
        PatchContext.SearchResult result = ctx.search(
                LDC(Type.getType("Lcom/eloraam/redpower/base/GuiAdvBench;")),
                NEW("codechicken/nei/recipe/DefaultOverlayHandler"),
                DUP(),
                BIPUSH(23),
                BIPUSH(12),
                INVOKESPECIAL("codechicken/nei/recipe/DefaultOverlayHandler", "<init>", "(II)V"),
                LDC("crafting"),
                INVOKESTATIC("codechicken/nei/api/API", "registerGuiOverlayHandler", "(Ljava/lang/Class;Lcodechicken/nei/api/IOverlayHandler;Ljava/lang/String;)V")
        );
        result.jumpBefore();
        result.erase();
        ctx.add(
                LDC(Type.getType("Lcom/eloraam/redpower/base/GuiAdvBench;")),
                NEW(advBenchOverlayHandler()),
                DUP(),
                BIPUSH(23),
                BIPUSH(12),
                INVOKESPECIAL(advBenchOverlayHandler(), "<init>", "(II)V"),
                LDC("crafting"),
                INVOKESTATIC("codechicken/nei/api/API", "registerGuiOverlayHandler", "(Ljava/lang/Class;Lcodechicken/nei/api/IOverlayHandler;Ljava/lang/String;)V")
        );
    }

    private String advBenchOverlayHandler() {
        return getClass().getName().replace('.', '/') + "$AdvBenchOverlayHandler";
    }

    public static class AdvBenchOverlayHandler extends DefaultOverlayHandler {
        public AdvBenchOverlayHandler(int x, int y) {
            super(x, y);
        }

        @Override
        public void overlayRecipe(HandledScreen gui, List ingredients, boolean shift) {
            List<DistributedIngred> ingredStacks = getPermutationIngredients(ingredients);

            findInventoryQuantitiesAdvBench(gui, ingredStacks);
            clearCraftingGrid(gui);

            List<IngredientDistribution> assignedIngredients = assignIngredients(ingredients, ingredStacks);
            if (assignedIngredients == null)
                return;

            Slot[][] recipeSlots = assignIngredSlots(gui, ingredients, assignedIngredients);
            int quantity = calculateRecipeQuantity(assignedIngredients);

            if (quantity != 0) {
                moveIngredientsAdvBench(gui, assignedIngredients, quantity);
            }
        }

        private void clearCraftingGrid(HandledScreen gui) {
            for (int i=0; i<9; i++) {
                FastTransferManger.clickSlot(gui, i, 0, 1);
            }
        }

        private void findInventoryQuantitiesAdvBench(HandledScreen gui, List<DistributedIngred> ingredStacks) {
            for (Slot slot : (List<Slot>) gui.inventorySlots.inventorySlots)//work out how much we have to go round
            {
                if (slot.hasStack()) {
                    ItemStack pstack = slot.getStack();
                    DistributedIngred istack = findIngred(ingredStacks, pstack);
                    if (istack != null)
                        istack.invAmount += pstack.count;
                }
            }
        }


        private void moveIngredientsAdvBench(HandledScreen gui, List<IngredientDistribution> assignedIngredients, int quantity) {
            for (IngredientDistribution distrib : assignedIngredients) {
                ItemStack pstack = distrib.permutation;
                int transferCap = quantity * pstack.count;
                int transferred = 0;

                int destSlotIndex = 0;
                if (distrib.slots.length < 1)
                    continue;
                Slot dest = distrib.slots[0];
                int slotTransferred = 0;
                int slotTransferCap = pstack.getMaxCount();

                for (Slot slot : (List<Slot>) gui.inventorySlots.inventorySlots) {
                    if (!slot.hasStack() || assignedIngredients.stream().anyMatch(ingredient -> Arrays.asList(ingredient.slots).contains(slot)))
                        continue;

                    ItemStack stack = slot.getStack();
                    if (!InventoryUtils.canStack(stack, pstack))
                        continue;

                    FastTransferManger.clickSlot(gui, slot.slotNumber);
                    int amount = Math.min(transferCap - transferred, stack.count);
                    for (int c = 0; c < amount; c++) {
                        FastTransferManger.clickSlot(gui, dest.slotNumber, 1);
                        transferred++;
                        slotTransferred++;
                        if (slotTransferred >= slotTransferCap) {
                            destSlotIndex++;
                            if (destSlotIndex == distrib.slots.length) {
                                dest = null;
                                break;
                            }
                            dest = distrib.slots[destSlotIndex];
                            slotTransferred = 0;
                        }
                    }
                    FastTransferManger.clickSlot(gui, slot.slotNumber);
                    if (transferred >= transferCap || dest == null)
                        break;
                }
            }
        }
    }
}
