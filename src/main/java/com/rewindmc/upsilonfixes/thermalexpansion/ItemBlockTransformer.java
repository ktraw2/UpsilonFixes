package com.rewindmc.upsilonfixes.thermalexpansion;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
import thermalexpansion.transport.tileentity.TileTeleportRoot;

@Patch.Class("net.minecraft.src.ItemBlock")
public class ItemBlockTransformer extends UpsilonMiniTransformer {

	@Patch.Method("onItemUse(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;IIIIFFF)Z")
	public void patchGenerate(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.search(INVOKEVIRTUAL("net/minecraft/src/Block", "onBlockPlacedBy", "(Lnet/minecraft/src/World;IIILnet/minecraft/src/EntityLiving;)V")).jumpAfter();
		LabelNode label = new LabelNode();
		ctx.add(
				ALOAD(0),
				INSTANCEOF("thermalexpansion/transport/block/ItemBlockTeleport"),
				IFEQ(label),
				ALOAD(1),
				ALOAD(3),
				ILOAD(4),
				ILOAD(5),
				ILOAD(6),
				INVOKESTATIC(hooks(), "placeBlockAt", "(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;III)V"),
				label
		);
	}

	public static class Hooks {
		public static void placeBlockAt(ItemStack stack, World world, int x, int y, int z) {
			TileTeleportRoot tile = (TileTeleportRoot) world.getBlockTileEntity(x, y, z);
			if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("TeleFreq")) {
				tile.frequency = stack.stackTagCompound.getInteger("TeleFreq");
			}
		}
	}
	
}