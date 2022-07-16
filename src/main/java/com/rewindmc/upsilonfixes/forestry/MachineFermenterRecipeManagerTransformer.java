package com.rewindmc.upsilonfixes.forestry;

import com.rewindmc.upsilonfixes.UpsilonFixesPremain;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("forestry.factory.gadgets.MachineFermenter$RecipeManager")
public class MachineFermenterRecipeManagerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("addRecipe(Lnet/minecraft/src/ItemStack;IFLnet/minecraftforge/liquids/LiquidStack;Lnet/minecraftforge/liquids/LiquidStack;)V")
	@Patch.Method.AffectsControlFlow
	public void patchAddRecipe1(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lcontinue1 = new LabelNode();
		LabelNode Lcontinue2 = new LabelNode();
		LabelNode Lexit = new LabelNode();
		ctx.add(
			ALOAD(4),
			INVOKESTATIC(hooks(), "checkNull", "(Ljava/lang/Object;)Z"),
			IFZ(Lcontinue1),
			GOTO(Lexit),
			Lcontinue1,
			ALOAD(5),
			INVOKESTATIC(hooks(), "checkNull", "(Ljava/lang/Object;)Z"),
			IFZ(Lcontinue2),
			Lexit,
			RETURN(),
			Lcontinue2
		);
	}

	@Patch.Method("addRecipe(Lnet/minecraft/src/ItemStack;IFLnet/minecraftforge/liquids/LiquidStack;)V")
	@Patch.Method.AffectsControlFlow
	public void patchAddRecipe2(PatchContext ctx) {
		// This method is just bad and poorly considered. Why does it exist?
		ctx.jumpToStart();
		ctx.add(
			INVOKESTATIC(hooks(), "warn", "()V"),
			RETURN()
		);
	}
	
	public static class Hooks {
		
		public static boolean checkNull(Object o) {
			if (o == null) {
				warn();
				return true;
			}
			return false;
		}
		
		public static void warn() {
			UpsilonFixesPremain.log.warn("Preventing registration of bad Forestry fermenter recipe with a null liquid", new Throwable("Stack trace").fillInStackTrace());
		}
		
	}
	
}
