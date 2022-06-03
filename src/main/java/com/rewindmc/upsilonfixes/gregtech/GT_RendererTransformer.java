package com.rewindmc.upsilonfixes.gregtech;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("gregtechmod.common.render.GT_Renderer")
public class GT_RendererTransformer extends UpsilonMiniTransformer {

	@Patch.Method("a(Lqx;F)V")
	public void patchRender(PatchContext ctx) {
		// Uses multiple no longer working Dropbox public URLs.
		ctx.jumpToStart();
		ctx.add(
			RETURN()
		);
	}
	
}
