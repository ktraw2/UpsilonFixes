package com.rewindmc.upsilonfixes.logisticspipes;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.PatchContext.SearchResult;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("logisticspipes.modules.ModuleElectricManager")
@ConfigOptions("fixLPElectricManagerNPE")
public class ModuleElectricManagerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("isOfInterest(Lnet/minecraft/item/ItemStack;)Z")
	public void patchIsOfInterest(PatchContext ctx) {
		SearchResult sr = ctx.search(
			INVOKEVIRTUAL("java/lang/String", "equals", "(Ljava/lang/Object;)Z")
		);
		sr.jumpBefore();
		sr.erase();
		ctx.add(INVOKESTATIC("java/util/Objects", "equals", "(Ljava/lang/Object;Ljava/lang/Object;)Z"));
	}
	
}
