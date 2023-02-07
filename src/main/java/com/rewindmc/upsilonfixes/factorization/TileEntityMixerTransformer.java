package com.rewindmc.upsilonfixes.factorization;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("factorization.common.TileEntityMixer")
@ConfigOptions("fixFzMixerInfiniteLoop")
public class TileEntityMixerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("drainBuffer()Z")
	public void patchDrainBuffer(PatchContext ctx) {
		// The second to last return should be false, to indicate to the caller to terminate the
		// loop, as there's nothing to be done this tick.
		ctx.jumpToEnd();
		ctx.searchBackward(
			ICONST_1(),
			IRETURN()
		).jumpBefore();
		ctx.jumpForward(1);
		ctx.add(
			POP(),
			ICONST_0()
		);
	}
	
}
