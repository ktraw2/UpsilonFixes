package com.rewindmc.upsilonfixes.codechicken;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("codechicken.core.asm.FeatureHackTransformer")
public class FeatureHackTransformerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("transformer003(Ljava/lang/String;[B)[B")
	@Patch.Method.AffectsControlFlow
	public void patchTransformer003(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(
			ALOAD(2),
			ARETURN()
		);
	}
	
}
