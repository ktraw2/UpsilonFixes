package com.rewindmc.upsilonfixes.dartcraft;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("bluedart.core.DartCraftCore")
@ConfigOptions("fixDartCraftForceDisablingGregTechTweaks")
public class DartCraftCoreTransformer extends UpsilonMiniTransformer {

	@Patch.Method("postInit()V")
	public void patchPostInit(PatchContext ctx) {
		ctx.search(
			LDC("gregtechmod.GT_Mod")
		).jumpAfter();
		ctx.add(
			POP(),
			LDC("thisis.aclass.that.will.not.exist.AndIfItDoes$ThenWellCongratulations")
		);
	}
	
}
