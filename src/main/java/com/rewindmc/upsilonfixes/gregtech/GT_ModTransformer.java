package com.rewindmc.upsilonfixes.gregtech;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("gregtechmod.GT_Mod")
@ConfigOptions("buffGregTechJackHammers")
public class GT_ModTransformer extends UpsilonMiniTransformer {

	@Patch.Method("load(Lcpw/mods/fml/common/event/FMLInitializationEvent;)V")
	public void patchLoad(PatchContext ctx) {
		ctx.search(
			BIPUSH(39),
			IALOAD(),
			SIPUSH(10000),
			ICONST_1(),
			LDC(7.5f)
		).jumpAfter();
		ctx.add(
			POP(),
			LDC(10.1f)
		);
		
		ctx.search(
			ICONST_1(),
			LDC(15.0f)
		).jumpAfter();
		ctx.add(
			POP(),
			LDC(30.1f)
		);
		
		ctx.search(
			ICONST_2(),
			LDC(45.0f)
		).jumpAfter();
		ctx.add(
			POP(),
			LDC(60.1f)
		);
	}
	
}
