package com.unascribed.upsilonfixes;

import com.unascribed.ears.common.agent.mini.MiniTransformer;
import com.unascribed.ears.common.agent.mini.PatchContext;
import com.unascribed.ears.common.agent.mini.annotation.Patch;

import cpw.mods.fml.relauncher.IClassTransformer;

public abstract class ThreadDisableTransformer extends MiniTransformer implements IClassTransformer {

	@Patch.Method("run()V")
	public void patchRun(PatchContext ctx) {
		// There are quite a few things we want to patch in exactly the same way, so here's a shortcut
		ctx.jumpToStart();
		ctx.add(
			RETURN()
		);
	}
	
}