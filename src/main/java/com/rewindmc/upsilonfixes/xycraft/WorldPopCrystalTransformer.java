package com.rewindmc.upsilonfixes.xycraft;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("soaryn.xycraft.world.gen.WorldPopCrystal")
public class WorldPopCrystalTransformer extends MiniTransformer {

	@Patch.Method("a(Lyc;Ljava/util/Random;III)Z")
	public void patchGenerate(PatchContext ctx) {
		// Go away, quartz crystals. No one likes you.
		ctx.jumpToStart();
		ctx.add(
			ICONST_1(),
			IRETURN()
		);
	}
	
}