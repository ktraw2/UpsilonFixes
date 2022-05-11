package com.unascribed.upsilonfixes.neiplugins;

import com.unascribed.upsilonfixes.ThreadDisableTransformer;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("mistaqur.nei.common.VersionCheck$VersionCheckThread")
public class VersionCheckThreadTransformer extends ThreadDisableTransformer {

	// Uses a Dropbox public URL that doesn't exist anymore
	
}
