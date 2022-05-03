package com.unascribed.upsilonfixes.gregtech;

import com.unascribed.ears.common.agent.mini.MiniTransformer;
import com.unascribed.ears.common.agent.mini.PatchContext;
import com.unascribed.ears.common.agent.mini.annotation.Patch;
import cpw.mods.fml.relauncher.IClassTransformer;

@Patch.Class("gregtechmod.common.render.GT_Renderer")
public class GT_RendererTransformer extends MiniTransformer implements IClassTransformer {

	@Patch.Method("a(Lqx;F)V")
	public void patchRender(PatchContext ctx) {
		// Uses multiple no longer working Dropbox public URLs.
		ctx.jumpToStart();
		ctx.add(
			RETURN()
		);
	}
	
}
