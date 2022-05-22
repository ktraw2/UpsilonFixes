package com.rewindmc.upsilonfixes.buildcraft;

import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("buildcraft.core.utils.BlockUtil")
public class BlockUtilTransformer extends MiniTransformer {

	@Patch.Method("isSoftBlock(Lnet/minecraft/src/World;III)Z")
	@Patch.Method.AffectsControlFlow
	public void patchFill(PatchContext ctx) {
		ctx.jumpToStart();

		// replace hardness 0 blocks
		
		LabelNode Lpopcontinue = new LabelNode();
		LabelNode Lcontinue = new LabelNode();
		ctx.add(
			GETSTATIC("net/minecraft/src/Block", "blocksList", "[Lnet/minecraft/src/Block;"),
			ALOAD(0),
			ILOAD(1),
			ILOAD(2),
			ILOAD(3),
			INVOKEVIRTUAL("net/minecraft/src/World", "getBlockId", "(III)I"),
			AALOAD(),
			DUP(),
			IFNULL(Lpopcontinue),
			ALOAD(0),
			ILOAD(1),
			ILOAD(2),
			ILOAD(3),
			INVOKEVIRTUAL("net/minecraft/src/Block", "getBlockHardness", "(Lnet/minecraft/src/World;III)F"),
			FCONST_0(),
			FCMPG(),
			IFNE(Lcontinue),
			ICONST_1(),
			IRETURN(),
			Lpopcontinue,
			POP(),
			Lcontinue
		);
	}
	
}
