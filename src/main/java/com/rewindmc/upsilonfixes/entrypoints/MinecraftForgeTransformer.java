package com.rewindmc.upsilonfixes.entrypoints;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraftforge.common.MinecraftForge")
public class MinecraftForgeTransformer extends UpsilonMiniTransformer {

	@Patch.Method("initialize()V")
	public void patchInitialize(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.addFireEntrypoint("forge-earlyinit");
	}
	
}
