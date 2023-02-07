package com.rewindmc.upsilonfixes.xycraft;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("soaryn.xycraft.world.gen.WorldPopCrystal")
@ConfigOptions("disableXycraftQuartzCrystalWorldgen")
public class WorldPopCrystalTransformer extends UpsilonMiniTransformer {

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