package com.unascribed.upsilonfixes.cofh;

import com.unascribed.ears.common.agent.mini.annotation.Patch;
import com.unascribed.upsilonfixes.ThreadDisableTransformer;

@Patch.Class("cofh.version.VersionInfo$VersionCheckThread")
public class VersionCheckThreadTransformer extends ThreadDisableTransformer {

	// Most uses of this try to use Dropbox public links
	
}
