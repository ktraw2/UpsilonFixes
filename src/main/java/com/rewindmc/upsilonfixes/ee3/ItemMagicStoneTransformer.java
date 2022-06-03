package com.rewindmc.upsilonfixes.ee3;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

public abstract class ItemMagicStoneTransformer extends UpsilonMiniTransformer {

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
