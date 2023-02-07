package com.rewindmc.upsilonfixes.layering;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.texturepacks.TexturePackList")
@ConfigOptions("layeredTexturePacks")
public class TexturePackListTransformer extends UpsilonMiniTransformer {

	@Patch.Method("updateAvaliableTexturePacks()V")
	public void patchGetResourceAsStream(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.add(
			ALOAD(1),
			INVOKESTATIC("com/rewindmc/upsilonfixes/layering/Layering", "refresh", "(Ljava/util/List;)V")
		);
	}
	
}
