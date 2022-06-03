package com.rewindmc.upsilonfixes.thaumicbees;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("thaumicbees.bees.AlleleEffectAuraNodeFlux")
public class AlleleEffectAuraNodeFluxTransformer extends UpsilonMiniTransformer {

	@Patch.Method("doEffect(Lforestry/api/apiculture/IBeeGenome;Lforestry/api/genetics/IEffectData;Lforestry/api/apiculture/IBeeHousing;)Lforestry/api/genetics/IEffectData;")
	public void patchDoEffect(PatchContext ctx) {
		ctx.search(
			GETSTATIC("thaumcraft/api/EnumTag", "WEATHER", "Lthaumcraft/api/EnumTag;")
		).jumpAfter();
		
		ctx.add(
			POP(),
			ACONST_NULL()
		);
	}
	
}
