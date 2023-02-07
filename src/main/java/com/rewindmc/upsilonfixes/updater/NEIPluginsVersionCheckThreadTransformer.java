package com.rewindmc.upsilonfixes.updater;

import com.rewindmc.upsilonfixes.ConfigOptions;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("mistaqur.nei.common.VersionCheck$VersionCheckThread")
@ConfigOptions("removeVersionCheckers")
public class NEIPluginsVersionCheckThreadTransformer extends ThreadDisableTransformer {

	// Uses a Dropbox public URL that doesn't exist anymore
	
}
