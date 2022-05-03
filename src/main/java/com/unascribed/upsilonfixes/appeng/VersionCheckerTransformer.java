package com.unascribed.upsilonfixes.appeng;

import com.unascribed.ears.common.agent.mini.annotation.Patch;
import com.unascribed.upsilonfixes.ThreadDisableTransformer;

@Patch.Class("appeng.common.VersionChecker")
public class VersionCheckerTransformer extends ThreadDisableTransformer {

	// URL is now a redirect, and AE puts the HTML source to the redirect into chat...
	
}
