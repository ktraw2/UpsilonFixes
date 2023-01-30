package com.rewindmc.upsilonfixes.asm;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.PatchContext.SearchResult;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("org.objectweb.asm.ClassReader")
public class ClassReaderTransformer extends UpsilonMiniTransformer {

	@Patch.Method("<init>([BII)V")
	public void patchConstructor(PatchContext ctx) {
		SearchResult res = ctx.search(BIPUSH(51));
		if (res.isSuccessful()) {
			res.jumpAfter();
			ctx.add(
				POP(),
				BIPUSH(52)
			);
		}
	}
	
}
