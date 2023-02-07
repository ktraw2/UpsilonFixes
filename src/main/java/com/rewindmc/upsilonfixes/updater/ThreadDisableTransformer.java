package com.rewindmc.upsilonfixes.updater;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

public abstract class ThreadDisableTransformer extends UpsilonMiniTransformer {

	@Patch.Method("run()V")
	public void patchRun(PatchContext ctx) {
		// There are quite a few things we want to patch in exactly the same way, so here's a shortcut
		ctx.jumpToStart();
		ctx.add(
			RETURN()
		);
	}
	
}