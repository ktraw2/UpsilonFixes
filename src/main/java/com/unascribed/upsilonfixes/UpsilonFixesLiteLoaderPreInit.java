package com.unascribed.upsilonfixes;

import java.util.logging.Logger;


public class UpsilonFixesLiteLoaderPreInit implements Runnable {

	@Override
	public void run() {
		if (UpsilonFixesConfig.enableLiteLoaderLogFix) {
			Logger l = Logger.getLogger("liteloader");
			l.removeHandler(l.getHandlers()[0]); // console handler
			l.setUseParentHandlers(true);
			l.setParent(Logger.getLogger("ForgeModLoader"));
		}
	}
	
}
