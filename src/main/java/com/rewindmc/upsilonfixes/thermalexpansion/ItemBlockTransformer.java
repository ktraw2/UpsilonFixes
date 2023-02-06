package com.rewindmc.upsilonfixes.thermalexpansion;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
import thermalexpansion.transport.tileentity.TileTeleportRoot;

@Patch.Class("net.minecraft.src.ItemBlock")
public class ItemBlockTransformer extends UpsilonMiniTransformer {

	@Patch.Method("placeBlockAt(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIIFFFI)Z")
	@Patch.Method.AffectsControlFlow
	public void patchPlaceBlockAt(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.search(
			INVOKEVIRTUAL("net/minecraft/block/Block", "onBlockPlacedBy", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLiving;)V")
		).jumpAfter();
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
			INVOKESTATIC(hooks(), "placeBlockAt", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;III)V"),
			label
		);
	}

	public static class Hooks {
		public static void placeBlockAt(ItemStack stack, World world, int x, int y, int z) {
			TileTeleportRoot tile = (TileTeleportRoot) world.getBlockTileEntity(x, y, z);
			if (stack.stackTagCompound != null) {
				tile.removeFromRegistry();
				if (stack.stackTagCompound.hasKey("TeleMode")) {
					tile.mode = stack.stackTagCompound.getByte("TeleMode");
				}
				if (stack.stackTagCompound.hasKey("TeleFreq")) {
					tile.frequency = stack.stackTagCompound.getInteger("TeleFreq");
					tile.isActive = true;
				}
				tile.addToRegistry();
				//world.markBlockForUpdate(x, y, z);
				//world.notifyBlocksOfNeighborChange(x, y, z, ThermalExpansionTransport.blockTeleport.blockID);
			}
		}
	}

}