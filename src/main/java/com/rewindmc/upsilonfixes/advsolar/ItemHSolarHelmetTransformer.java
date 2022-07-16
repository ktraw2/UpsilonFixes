package com.rewindmc.upsilonfixes.advsolar;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import com.rewindmc.upsilonfixes.gravisuite.ItemUltimateSolarHelmetTransformer;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("advsolar.ItemHSolarHelmet")
public class ItemHSolarHelmetTransformer extends UpsilonMiniTransformer {

	@Patch.Method("onArmorTickUpdate(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/ItemStack;)V")
	public void patchOnTick(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(
				ALOAD(2),
				ALOAD(3),
				INVOKESTATIC(hooks(), "feedPlayer", "(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/ItemStack;)V")
		);
	}

	public static class Hooks {

		public static void feedPlayer(EntityPlayer player, ItemStack itemStack) {
			ItemUltimateSolarHelmetTransformer.Hooks.feedPlayer(player, itemStack);
		}
		
	}
	
}
