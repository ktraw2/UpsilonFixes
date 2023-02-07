package com.rewindmc.upsilonfixes.updater;

import com.rewindmc.upsilonfixes.ConfigOptions;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("cofh.version.VersionInfo$VersionCheckThread")
@ConfigOptions("removeVersionCheckers")
public class CoFHVersionCheckThreadTransformer extends ThreadDisableTransformer {

	// Most uses of this try to use Dropbox public links
	
}
