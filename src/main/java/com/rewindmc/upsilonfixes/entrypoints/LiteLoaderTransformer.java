package com.rewindmc.upsilonfixes.entrypoints;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.mumfrey.liteloader.core.LiteLoader")
public class LiteLoaderTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("prepareLoader()Z")
	public void prepareLoader(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.addFireEntrypoint("liteloader-preinit");
	}
	
}
