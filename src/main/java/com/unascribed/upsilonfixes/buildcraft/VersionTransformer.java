package com.unascribed.upsilonfixes.buildcraft;

import com.unascribed.ears.common.agent.mini.annotation.Patch;
import com.unascribed.upsilonfixes.ThreadDisableTransformer;

@Patch.Class("buildcraft.core.Version")
public class VersionTransformer extends ThreadDisableTransformer {

	// URL still works, but is interpreted incorrectly and always says outdated
	
}
