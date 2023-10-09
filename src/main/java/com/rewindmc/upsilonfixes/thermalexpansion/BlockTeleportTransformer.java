package com.rewindmc.upsilonfixes.thermalexpansion;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
import thermalexpansion.transport.tileentity.TileTeleportRoot;

@Patch.Class("thermalexpansion.transport.block.BlockTeleport")
@ConfigOptions({"keepTesseractFrequencyOnDismantle", "fixThermalTesseractCast"})
public class BlockTeleportTransformer extends UpsilonMiniTransformer {

	@Patch.Method("dismantleBlock(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIZ)Lnet/minecraft/item/ItemStack;")
	public void patchDismantleBlock(PatchContext ctx) {
		if (UpsilonFixesConfig.keepTesseractFrequencyOnDismantle) {
			ctx.jumpToLastReturn();
			ctx.add(
				ALOAD(7),
				INVOKESTATIC(hooks(), "addFreq", "(Lnet/minecraft/item/ItemStack;Lthermalexpansion/transport/tileentity/TileTeleportRoot;)Lnet/minecraft/item/ItemStack;")
			);
		}
	}
	
	@Patch.Method("a(Lnet/minecraft/world/World;IIIII)V")
	@Patch.Method("a(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLiving;)V")
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
				INVOKESTATIC(hooks(), "checkTEType", "(Lnet/minecraft/world/World;III)Z"),
				IFNZ(Lcontinue),
				RETURN(),
				Lcontinue
			);
		}
	}
	
	@Patch.Method("a(Lnet/minecraft/world/World;IIILnet/minecraft/entity/player/EntityPlayer;IFFF)Z")
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
				INVOKESTATIC(hooks(), "checkTEType", "(Lnet/minecraft/world/World;III)Z"),
				IFNZ(Lcontinue),
				ICONST_0(),
				IRETURN(),
				Lcontinue
			);
		}
	}

	public static class Hooks {
		
		public static ItemStack addFreq(ItemStack stack, TileTeleportRoot tile) {
			if (stack.tag == null) stack.tag = new NbtCompound();
			stack.tag.putInt("TeleFreq", tile.frequency);
			stack.tag.putByte("TeleMode", tile.mode);
			stack.tag.putByte("TeleAccess", tile.access);
			return stack;
		}
		
		public static boolean checkTEType(World world, int x, int y, int z) {
			return world.getBlockTileEntity(x, y, z) instanceof TileTeleportRoot;
		}
		
	}
	
}