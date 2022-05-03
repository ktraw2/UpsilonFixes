package com.unascribed.upsilonfixes.xycraft;

import com.unascribed.ears.common.agent.mini.MiniTransformer;
import com.unascribed.ears.common.agent.mini.PatchContext;
import com.unascribed.ears.common.agent.mini.annotation.Patch;

import cpw.mods.fml.relauncher.IClassTransformer;

@Patch.Class("soaryn.xycraft.world.gen.WorldPopCrystal")
public class WorldPopCrystalTransformer extends MiniTransformer implements IClassTransformer {

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