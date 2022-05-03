package com.unascribed.upsilonfixes.ic2;

import com.unascribed.ears.common.agent.mini.MiniTransformer;
import com.unascribed.ears.common.agent.mini.PatchContext;
import com.unascribed.ears.common.agent.mini.annotation.Patch;
import cpw.mods.fml.relauncher.IClassTransformer;

@Patch.Class("ic2.core.PlatformClient")
public class PlatformClientTransformer extends MiniTransformer implements IClassTransformer {

	@Patch.Method("run()V")
	public void patchRun(PatchContext ctx) {
		// downloads a cape list from an obfuscated URL (https://rg.dl.je/jzYcbjmOP2Y947yVCOX37EFnlxuXhj.txt)
		// doesn't work anymore
		ctx.jumpToStart();
		ctx.add(
				RETURN()
		);
	}
	
}
