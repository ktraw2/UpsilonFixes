package com.rewindmc.upsilonfixes.updater;

import com.rewindmc.upsilonfixes.ConfigOptions;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("appeng.common.VersionChecker")
@ConfigOptions("removeVersionCheckers")
public class AppEngVersionCheckerTransformer extends ThreadDisableTransformer {

	// URL is now a redirect, and AE puts the HTML source to the redirect into chat...
	
}
