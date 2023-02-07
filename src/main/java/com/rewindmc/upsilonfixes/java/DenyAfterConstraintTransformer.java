package com.rewindmc.upsilonfixes.java;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("sun.security.util.DisabledAlgorithmConstraints$DenyAfterConstraint")
@ConfigOptions("reenableSha1Signatures")
public class DenyAfterConstraintTransformer extends UpsilonMiniTransformer {

	@Patch.Method("permits(Lsun/security/util/ConstraintsParameters;)V")
	@Patch.Method("permits(Ljava/security/Key;)Z")
	@Patch.Method.AffectsControlFlow
	public void patchPermits(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(RETURN());
	}
	
}
