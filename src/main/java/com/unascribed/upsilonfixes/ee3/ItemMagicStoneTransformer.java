package com.unascribed.upsilonfixes.ee3;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

public abstract class ItemMagicStoneTransformer extends MiniTransformer {

	@Patch.Method("getContainerItemStack(Lur;)Lur;")
	public void patchGetContainerItemStack(PatchContext ctx) {
		// Return a *copy* of the stack, rather than the stack itself.
		ctx.search(ALOAD(1)).jumpAfter();;
		ctx.add(
			INVOKEVIRTUAL("ur", "l", "()Lur;"), // ItemStack.copy (ItemStack)
			DUP(),
			ASTORE(1)
		);
	}
	
}
