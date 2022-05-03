package com.unascribed.upsilonfixes.ee3;

import com.unascribed.ears.common.agent.mini.MiniTransformer;
import com.unascribed.ears.common.agent.mini.PatchContext;
import com.unascribed.ears.common.agent.mini.annotation.Patch;
import cpw.mods.fml.relauncher.IClassTransformer;

@Patch.Classes({
	@Patch.Class("com.pahimar.ee3.item.ItemPhilosopherStone"),
	@Patch.Class("com.pahimar.ee3.item.ItemMiniumStone")
})
public class ItemMagicStoneTransformer extends MiniTransformer implements IClassTransformer {

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
