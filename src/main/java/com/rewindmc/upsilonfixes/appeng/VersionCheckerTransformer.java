package com.rewindmc.upsilonfixes.appeng;

import com.rewindmc.upsilonfixes.ThreadDisableTransformer;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("appeng.common.VersionChecker")
public class VersionCheckerTransformer extends ThreadDisableTransformer {

	// URL is now a redirect, and AE puts the HTML source to the redirect into chat...
	
}
