package com.rewindmc.upsilonfixes.gravisuite;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("gravisuite.ClientTickHandler")
public class ClientTickHandlerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("tickStart(Ljava/util/EnumSet;[Ljava/lang/Object;)V")
	public void patchTickStart(PatchContext ctx) {
		ctx.search(LDC("Boost Key")).jumpAfter();
		ctx.add(
			POP(),
			LDC("Sprint/Boost Key")
		);
	}
	
}
