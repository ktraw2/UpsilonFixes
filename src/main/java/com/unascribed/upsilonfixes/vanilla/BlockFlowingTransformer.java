package com.unascribed.upsilonfixes.vanilla;

import com.unascribed.ears.common.agent.mini.MiniTransformer;
import com.unascribed.ears.common.agent.mini.PatchContext;
import com.unascribed.ears.common.agent.mini.annotation.Patch;

import cpw.mods.fml.relauncher.IClassTransformer;

@Patch.Class("aky")
public class BlockFlowingTransformer extends MiniTransformer implements IClassTransformer {

	@Patch.Method("b(Lyc;IIILjava/util/Random;)V")
	public void patchUpdateTick(PatchContext ctx) {
		// It turns out water whirlpools prior to 1.5 are a *bug*!
		// 1.4 code:
		// } else if (world.getBlockMaterial(x, y - 1, z) == this.blockMaterial && world.getBlockMetadata(x, y, z) == 0) {
		// 1.5 code:
		// } else if (world.getBlockMaterial(x, y - 1, z) == this.blockMaterial && world.getBlockMetadata(x, y - 1, z) == 0) {
		// Notice that the block metadata of the *current* block is read instead of the one below that we just checked! Oops!

		ctx.search(
			ALOAD(1),
			ILOAD(2),
			ILOAD(3),
			ILOAD(4),
			INVOKEVIRTUAL("yc", "h", "(III)I") // World.getBlockMetadata
		).jumpAfter();

		// All we need to do is insert the "- 1" part in the second argument...
		ctx.searchBackward(ILOAD(3)).jumpAfter();
		ctx.add(
			ICONST_1(),
			ISUB()
		);
	}

}
