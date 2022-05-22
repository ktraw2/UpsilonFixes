package com.rewindmc.upsilonfixes.codechicken;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("codechicken.core.asm.ClassHeirachyManager")
public class ClassHeirachyManagerTransformer extends MiniTransformer {

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
