package com.unascribed.upsilonfixes.cofh;

import com.unascribed.upsilonfixes.ThreadDisableTransformer;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("cofh.version.VersionInfo$VersionCheckThread")
public class VersionCheckThreadTransformer extends ThreadDisableTransformer {

	// Most uses of this try to use Dropbox public links
	
}
