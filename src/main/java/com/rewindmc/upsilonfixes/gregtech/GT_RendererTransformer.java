package com.rewindmc.upsilonfixes.gregtech;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("gregtechmod.common.render.GT_Renderer")
@ConfigOptions("removeDeadCosmetics")
public class GT_RendererTransformer extends UpsilonMiniTransformer {

	@Patch.Method("a(Lqx;F)V")
	public void patchRender(PatchContext ctx) {
		// Uses multiple no longer working Dropbox public URLs.
		ctx.search(
			INVOKESPECIAL("net/minecraft/client/renderer/entity/RenderPlayer", "renderSpecials", "(Lnet/minecraft/entity/player/EntityPlayer;F)V")
		).jumpAfter();
		ctx.add(
			RETURN()
		);
	}
	
}
