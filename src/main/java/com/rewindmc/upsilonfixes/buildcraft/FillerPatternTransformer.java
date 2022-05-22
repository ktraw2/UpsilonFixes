package com.rewindmc.upsilonfixes.buildcraft;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("buildcraft.builders.FillerPattern")
public class FillerPatternTransformer extends MiniTransformer {

	@Patch.Method("fill(IIIIIILnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;)Z")
	public void patchFill(PatchContext ctx) {
		// prevent filler from stopping if it hits an unbreakable block - just skip it instead
		ctx.search(
			INVOKESTATIC("buildcraft/core/utils/BlockUtil", "canChangeBlock", "(Lnet/minecraft/src/World;III)Z")
		).jumpAfter();
		ctx.add(
			POP(),
			ICONST_1()
		);

		// remove blocks before trying to place
		ctx.search(
				ALOAD(7),
				INVOKEVIRTUAL("net/minecraft/src/ItemStack", "getItem", "()Lnet/minecraft/src/Item;")
		).jumpBefore();
		ctx.add(
			ALOAD(8),
			ILOAD(10),
			ILOAD(11),
			ILOAD(12),
			ICONST_0(),
			INVOKEVIRTUAL("net/minecraft/src/World", "setBlockWithNotify", "(IIII)Z"),
			POP()
		);
	}
	
}
