package com.unascribed.upsilonfixes.neiplugins;

import com.unascribed.ears.common.agent.mini.annotation.Patch;
import com.unascribed.upsilonfixes.ThreadDisableTransformer;

@Patch.Class("mistaqur.nei.common.VersionCheck$VersionCheckThread")
public class VersionCheckThreadTransformer extends ThreadDisableTransformer {

	// Uses a Dropbox public URL that doesn't exist anymore
	
}
