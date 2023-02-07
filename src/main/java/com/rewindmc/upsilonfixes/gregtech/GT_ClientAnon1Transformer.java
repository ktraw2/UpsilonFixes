package com.rewindmc.upsilonfixes.gregtech;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.updater.ThreadDisableTransformer;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("gregtechmod.common.GT_Client$1")
@ConfigOptions("removeDeadCosmetics")
public class GT_ClientAnon1Transformer extends ThreadDisableTransformer {

	// Uses a Dropbox public link
	
}
