package com.rewindmc.upsilonfixes.layering;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.TexturePackList")
public class TexturePackListTransformer extends MiniTransformer {

	@Patch.Method("updateAvaliableTexturePacks()V")
	public void patchGetResourceAsStream(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.add(
			ALOAD(1),
			INVOKESTATIC("com/rewindmc/upsilonfixes/layering/Layering", "refresh", "(Ljava/util/List;)V")
		);
	}
	
}
