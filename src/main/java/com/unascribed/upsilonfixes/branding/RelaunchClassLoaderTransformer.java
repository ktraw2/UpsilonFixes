package com.unascribed.upsilonfixes.branding;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("cpw.mods.fml.relauncher.RelaunchClassLoader")
public class RelaunchClassLoaderTransformer extends MiniTransformer {

	@Patch.Method("<init>([Ljava/net/URL;)V")
	public void patchInitialize(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.add(
			ALOAD(0),
			LDC("nilloader."),
			INVOKESPECIAL("cpw/mods/fml/relauncher/RelaunchClassLoader", "addClassLoaderExclusion", "(Ljava/lang/String;)V")
		);
	}

}
