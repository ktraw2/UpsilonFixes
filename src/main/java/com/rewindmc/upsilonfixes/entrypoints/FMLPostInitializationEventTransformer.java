package com.rewindmc.upsilonfixes.entrypoints;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("cpw.mods.fml.common.event.FMLPostInitializationEvent")
public class FMLPostInitializationEventTransformer extends MiniTransformer {

	@Patch.Method("<init>([Ljava/lang/Object;)V")
	public void patchConstructor(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.addFireEntrypoint("forge-postinit");
	}
	
}
