package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.renderer.EntityRenderer")
@ConfigOptions("modernFpsSlider")
public class EntityRendererTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("performanceToFps(I)I")
	@Patch.Method.AffectsControlFlow
	public void patchPerformanceToFps(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(
			ILOAD(0),
			IRETURN()
		);
	}

}
