package com.rewindmc.upsilonfixes.thermalexpansion;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
import thermalexpansion.transport.tileentity.TileTeleportRoot;

@Patch.Class("thermalexpansion.transport.block.BlockTeleport")
public class BlockTeleportTransformer extends UpsilonMiniTransformer {

	@Patch.Method("dismantleBlock(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;IIIZ)Lnet/minecraft/src/ItemStack;")
	public void patchDismantleBlock(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.add(
			ALOAD(7),
			INVOKESTATIC(hooks(), "addFreq", "(Lnet/minecraft/src/ItemStack;Lthermalexpansion/transport/tileentity/TileTeleportRoot;)Lnet/minecraft/src/ItemStack;")
		);
	}

	public static class Hooks {
		
		public static ItemStack addFreq(ItemStack stack, TileTeleportRoot tile) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setInteger("TeleFreq", tile.frequency);
			stack.stackTagCompound.setByte("TeleMode", tile.mode);
			return stack;
		}
		
	}
	
}