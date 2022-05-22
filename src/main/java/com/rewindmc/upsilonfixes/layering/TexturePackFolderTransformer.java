package com.rewindmc.upsilonfixes.layering;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.TexturePackFolder")
public class TexturePackFolderTransformer extends TexturePackTransformer {
	
	@Override
	@Patch.Method("getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;")
	@Patch.Method.AffectsControlFlow
	public void patchGetResourceAsStream(PatchContext ctx) {
		super.patchGetResourceAsStream(ctx);
	}
	
}
