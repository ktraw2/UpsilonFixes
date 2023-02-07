package com.rewindmc.upsilonfixes.advsolar;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import com.rewindmc.upsilonfixes.gravisuite.ItemUltimateSolarHelmetTransformer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("advsolar.ItemHSolarHelmet")
@ConfigOptions("quantumSolarHelmCans")
public class ItemHSolarHelmetTransformer extends UpsilonMiniTransformer {

	@Patch.Method("onArmorTickUpdate(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)V")
	public void patchOnTick(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(
				ALOAD(2),
				ALOAD(3),
				INVOKESTATIC(hooks(), "feedPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)V")
		);
	}

	public static class Hooks {

		public static void feedPlayer(EntityPlayer player, ItemStack itemStack) {
			ItemUltimateSolarHelmetTransformer.Hooks.feedPlayer(player, itemStack);
		}
		
	}
	
}
