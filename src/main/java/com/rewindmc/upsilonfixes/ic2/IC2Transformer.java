package com.rewindmc.upsilonfixes.ic2;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("ic2.core.IC2")
@ConfigOptions("fixIC2SeasonalCrash")
public class IC2Transformer extends UpsilonMiniTransformer {

	@Patch.Method("onLivingSpecialSpawn(Lnet/minecraftforge/event/entity/living/LivingSpecialSpawnEvent;)V")
	public void patchOnLivingSpecialSpawn(PatchContext ctx) {
		ctx.search(ATHROW()).jumpBefore();
		ctx.add(RETURN());
	}
	
}
