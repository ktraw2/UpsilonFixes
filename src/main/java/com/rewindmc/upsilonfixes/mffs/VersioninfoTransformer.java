package com.rewindmc.upsilonfixes.mffs;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("chb.mods.mffs.common.Versioninfo")
public class VersioninfoTransformer extends MiniTransformer {

	@Patch.Method("newestversion()Ljava/lang/String;")
	public void patchNewestversion(PatchContext ctx) {
		// The URL used no longer belongs to the MFFS author, but someone else created a repo at the
		// same path with a dummy file that just says "meow", which is incredible. I've chosen to
		// keep the "meow" because I find it hilarious.
		ctx.jumpToStart();
		ctx.add(
			LDC("meow"),
			ARETURN()
		);
	}
	
}
