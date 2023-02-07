package com.rewindmc.upsilonfixes.layering;

import com.rewindmc.upsilonfixes.ConfigOptions;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.texturepacks.TexturePackFolder")
@ConfigOptions("layeredTexturePacks")
public class TexturePackFolderTransformer extends TexturePackTransformer {
	
	@Override
	@Patch.Method("getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;")
	@Patch.Method.AffectsControlFlow
	public void patchGetResourceAsStream(PatchContext ctx) {
		super.patchGetResourceAsStream(ctx);
	}
	
}
