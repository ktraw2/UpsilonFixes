package com.rewindmc.upsilonfixes;

import nilloader.api.lib.mini.MiniTransformer;

public abstract class UpsilonMiniTransformer extends MiniTransformer {

	protected final String hooks() {
		return getClass().getName().replace('.', '/')+"$Hooks";
	}
	
}
