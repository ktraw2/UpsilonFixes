package com.rewindmc.upsilonfixes.codechicken;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("codechicken.core.asm.ClassHeirachyManager")
@ConfigOptions("fixCodeChickenCoreHierarchyCheck")
public class ClassHeirachyManagerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("classExtends(Ljava/lang/String;Ljava/lang/String;)Z")
	public void patchClassExtends(PatchContext ctx) {
		ctx.search(
			NEW("java/lang/RuntimeException")
		).jumpAfter();
		
		ctx.add(
			ICONST_0(),
			IRETURN()
		);
	}
	
}
