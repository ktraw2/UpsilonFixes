package com.rewindmc.upsilonfixes.twifor;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.item.ItemStack;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("twilightforest.ContainerTFUncrafting")
@ConfigOptions("fixUncraftingExploits")
public class ContainerTFUncraftingTransformer extends UpsilonMiniTransformer {

	@Patch.Method("getRecipeFor(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/crafting/IRecipe;")
	@Patch.Method.AffectsControlFlow
	public void patchGetRecipeFor(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lcontinue = new LabelNode();
		ctx.add(
			ALOAD(1),
			INVOKESTATIC(hooks(), "isItemBanned", "(Lnet/minecraft/item/ItemStack;)Z"),
			IF_FALSE(Lcontinue),
			ACONST_NULL(),
			ARETURN(),
			Lcontinue
		);
	}
	
	public static class Hooks {
		
		public static boolean isItemBanned(ItemStack stack) {
			if (stack == null) return false;
			if ("item.GraviGun".equals(stack.getItemName()) && stack.getMetadata() != 0)
				return true;
			return false;
		}
		
	}
	
}
