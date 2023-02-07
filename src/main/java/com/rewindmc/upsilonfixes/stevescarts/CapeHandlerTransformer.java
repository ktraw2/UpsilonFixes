package com.rewindmc.upsilonfixes.stevescarts;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.updater.ThreadDisableTransformer;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("vswe.stevescarts.CapeHandler")
@ConfigOptions("removeDeadCosmetics")
public class CapeHandlerTransformer extends ThreadDisableTransformer {

	// Downloads a cape list from a Dropbox public URL
	
}
