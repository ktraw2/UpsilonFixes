package com.rewindmc.upsilonfixes.rp2;

import buildcraft.api.transport.IPipeEntry;
import com.eloraam.redpower.core.WorldCoord;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.eloraam.redpower.core.MachineLib")
public class MachineLibTransformer extends UpsilonMiniTransformer {

	@Patch.Method("addToInventoryCore(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lcom/eloraam/redpower/core/WorldCoord;IZ)Z")
	@Patch.Method.AffectsControlFlow
	public void patchPipesIntoRP(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode label = new LabelNode();
		ctx.add(
				ALOAD(0),
				ALOAD(1),
				ALOAD(2),
				ILOAD(3),
				ILOAD(4),
				INVOKESTATIC(hooks(), "pipeInsert", "(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lcom/eloraam/redpower/core/WorldCoord;IZ)Z"),
				IFEQ(label),
				ICONST_1(),
				IRETURN(),
				label
		);
	}

	public static class Hooks {
		public static boolean pipeInsert(World world, ItemStack ist, WorldCoord wc, int side, boolean act) {
			TileEntity te = world.getBlockTileEntity(wc.x, wc.y, wc.z);
			if (te instanceof IPipeEntry && ((IPipeEntry) te).acceptItems()) {
				if (act) {
					((IPipeEntry) te).entityEntering(ist.copy(), ForgeDirection.getOrientation(side).getOpposite());
				}
				return true;
			}
			return false;
		}
	}
	
}
