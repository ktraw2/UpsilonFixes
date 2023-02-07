package com.rewindmc.upsilonfixes.factorization;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("factorization.client.render.TileEntityBarrelRenderer")
@ConfigOptions("fixFzBarrelLighting")
public class TileEntityBarrelRendererTransformer extends UpsilonMiniTransformer {

	@Patch.Method("setupLight(Lfactorization/common/TileEntityBarrel;II)V")
	public void patchSetupLight(PatchContext ctx) {
		// I don't know why, but the light level gets multiplied by 0.6.
		// This looks okay on Bright but absolutely horrid on anything lower.
		ctx.jumpToStart();
		ctx.search(
			LDC(0.6f)
		).jumpAfter();
		ctx.add(
			POP(),
			LDC(1f)
		);
	}
	
}
