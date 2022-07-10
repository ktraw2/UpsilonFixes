package com.rewindmc.upsilonfixes.thermalexpansion;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import net.minecraft.src.ItemStack;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
import thermalexpansion.transport.tileentity.TileTeleportRoot;

@Patch.Class("thermalexpansion.transport.block.BlockTeleport")
public class BlockTeleportTransformer extends UpsilonMiniTransformer {

	@Patch.Method("dismantleBlock(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;IIIZ)Lnet/minecraft/src/ItemStack;")
	public void patchGenerate(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.search(ARETURN()).jumpBefore();
		ctx.add(
			ALOAD(7),
			INVOKESTATIC(hooks(), "addFreq", "(Lnet/minecraft/src/ItemStack;Lthermalexpansion/transport/tileentity/TileTeleportRoot;)Lnet/minecraft/src/ItemStack;"),
			ARETURN()
		);
	}

	public static class Hooks {
		public static ItemStack addFreq(ItemStack stack, TileTeleportRoot tile) {
			stack.stackTagCompound.setInteger("TeleFreq", tile.frequency);
			return stack;
		}
	}
	
}