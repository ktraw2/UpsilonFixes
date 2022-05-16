package com.rewindmc.upsilonfixes.buildcraft;

import com.rewindmc.upsilonfixes.ThreadDisableTransformer;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("buildcraft.core.Version")
public class VersionTransformer extends ThreadDisableTransformer {

	// URL still works, but is interpreted incorrectly and always says outdated
	
}
