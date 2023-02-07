package com.rewindmc.upsilonfixes.layering;

import com.rewindmc.upsilonfixes.ConfigOptions;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.texturepacks.TexturePackCustom")
@ConfigOptions("layeredTexturePacks")
public class TexturePackCustomTransformer extends TexturePackTransformer {
	
	@Override
	@Patch.Method("getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;")
	@Patch.Method.AffectsControlFlow
	public void patchGetResourceAsStream(PatchContext ctx) {
		super.patchGetResourceAsStream(ctx);
	}
	
}
