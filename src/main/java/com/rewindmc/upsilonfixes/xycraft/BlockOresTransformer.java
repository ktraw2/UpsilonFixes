package com.rewindmc.upsilonfixes.xycraft;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("soaryn.xycraft.world.block.BlockOres")
public class BlockOresTransformer extends UpsilonMiniTransformer {

	@Patch.Method("getAnimationIndex(Lnet/minecraft/world/IBlockAccess;IIILnet/minecraftforge/common/ForgeDirection;)I")
	@Patch.Method.AffectsControlFlow
	public void patchGetAnimationIndexPos(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(
			ALOAD(1),
			ILOAD(2),
			ILOAD(3),
			ILOAD(4),
			INVOKEINTERFACE("net/minecraft/world/IBlockAccess", "getBlockMetadata", "(III)I"),
			INVOKESTATIC(hooks(), "getAnimationIndex", "(I)I"),
			IRETURN()
		);
	}
	
	@Patch.Method("getAnimationIndex(ILnet/minecraftforge/common/ForgeDirection;)I")
	@Patch.Method.AffectsControlFlow
	public void patchGetAnimationIndexMeta(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(
			ILOAD(1),
			INVOKESTATIC(hooks(), "getAnimationIndex", "(I)I"),
			IRETURN()
		);
		
	}
	
	@Patch.Method("getItemColor(ILnet/minecraftforge/common/ForgeDirection;)Lcodechicken/xycraftcopy/core/colour/Colour;")
	public void patchGetItemColor(PatchContext ctx) {
		patchGetColorGeneric(ctx, 1);
	}
	
	@Patch.Method("getColor(Lnet/minecraft/world/IBlockAccess;IIILnet/minecraftforge/common/ForgeDirection;)Lcodechicken/xycraftcopy/core/colour/Colour;")
	public void patchGetColor(PatchContext ctx) {
		patchGetColorGeneric(ctx, 6);
	}
	
	
	private void patchGetColorGeneric(PatchContext ctx, int var) {
		ctx.search(
			GETSTATIC("soaryn/xycraft/core/lib/XyReferences", "xyColors", "[Lcodechicken/xycraftcopy/core/colour/Colour;"),
			ILOAD(var),
			ICONST_5(),
			IREM()
		).jumpAfter();
		
		ctx.add(
			POP(),
			ICONST_4()
		);
	}


	public static class Hooks {
		
		public static int getAnimationIndex(int meta) {
			if (meta >= 6) return 255;
			// this is hardcoded to 20, which always uses the light xychorium emissive texture
			return 16+meta;
		}
		
	}
	
}
