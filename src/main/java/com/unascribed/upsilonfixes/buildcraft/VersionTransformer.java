package com.unascribed.upsilonfixes.buildcraft;

import com.unascribed.upsilonfixes.ThreadDisableTransformer;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("buildcraft.core.Version")
public class VersionTransformer extends ThreadDisableTransformer {

	// URL still works, but is interpreted incorrectly and always says outdated
	
}
