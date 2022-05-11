package com.unascribed.upsilonfixes.gregtech;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("gregtechmod.common.render.GT_Renderer")
public class GT_RendererTransformer extends MiniTransformer {

	@Patch.Method("a(Lqx;F)V")
	public void patchRender(PatchContext ctx) {
		// Uses multiple no longer working Dropbox public URLs.
		ctx.jumpToStart();
		ctx.add(
			RETURN()
		);
	}
	
}
