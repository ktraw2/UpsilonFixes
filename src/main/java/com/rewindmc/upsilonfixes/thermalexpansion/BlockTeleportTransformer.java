package com.rewindmc.upsilonfixes.thermalexpansion;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
import thermalexpansion.transport.tileentity.TileTeleportRoot;

@Patch.Class("thermalexpansion.transport.block.BlockTeleport")
public class BlockTeleportTransformer extends UpsilonMiniTransformer {

	@Patch.Method("dismantleBlock(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/World;IIIZ)Lnet/minecraft/src/ItemStack;")
	public void patchDismantleBlock(PatchContext ctx) {
		if (UpsilonFixesConfig.keepTesseractFrequencyOnDismantle) {
			ctx.jumpToLastReturn();
			ctx.add(
				ALOAD(7),
				INVOKESTATIC(hooks(), "addFreq", "(Lnet/minecraft/src/ItemStack;Lthermalexpansion/transport/tileentity/TileTeleportRoot;)Lnet/minecraft/src/ItemStack;")
			);
		}
	}
	
	@Patch.Method("a(Lnet/minecraft/src/World;IIIII)V")
	@Patch.Method("a(Lnet/minecraft/src/World;IIILnet/minecraft/src/EntityLiving;)V")
	@Patch.Method.AffectsControlFlow
	public void patchUncheckedCastsVoid(PatchContext ctx) {
		if (UpsilonFixesConfig.fixThermalTesseractCast) {
			ctx.jumpToStart();
			LabelNode Lcontinue = new LabelNode();
			ctx.add(
				ALOAD(1),
				ILOAD(2),
				ILOAD(3),
				ILOAD(4),
				INVOKESTATIC(hooks(), "checkTEType", "(Lnet/minecraft/src/World;III)Z"),
				IFZ(Lcontinue),
				RETURN(),
				Lcontinue
			);
		}
	}
	
	@Patch.Method("a(Lnet/minecraft/src/World;IIILnet/minecraft/src/EntityPlayer;IFFF)Z")
	@Patch.Method.AffectsControlFlow
	public void patchUncheckedCastsBool(PatchContext ctx) {
		if (UpsilonFixesConfig.fixThermalTesseractCast) {
			ctx.jumpToStart();
			LabelNode Lcontinue = new LabelNode();
			ctx.add(
				ALOAD(1),
				ILOAD(2),
				ILOAD(3),
				ILOAD(4),
				INVOKESTATIC(hooks(), "checkTEType", "(Lnet/minecraft/src/World;III)Z"),
				IFZ(Lcontinue),
				ICONST_0(),
				IRETURN(),
				Lcontinue
			);
		}
	}

	public static class Hooks {
		
		public static ItemStack addFreq(ItemStack stack, TileTeleportRoot tile) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setInteger("TeleFreq", tile.frequency);
			stack.stackTagCompound.setByte("TeleMode", tile.mode);
			return stack;
		}
		
		public static boolean checkTEType(World world, int x, int y, int z) {
			return world.getBlockTileEntity(x, y, z) instanceof TileTeleportRoot;
		}
		
	}
	
}