package com.unascribed.upsilonfixes.ic2;

import com.unascribed.ears.common.agent.mini.annotation.Patch;
import com.unascribed.upsilonfixes.ThreadDisableTransformer;

@Patch.Class("ic2.core.PlatformClient")
public class PlatformClientTransformer extends ThreadDisableTransformer {

	// Downloads a cape list from an obfuscated URL (https://rg.dl.je/jzYcbjmOP2Y947yVCOX37EFnlxuXhj.txt)
	// Doesn't work anymore
	
}
