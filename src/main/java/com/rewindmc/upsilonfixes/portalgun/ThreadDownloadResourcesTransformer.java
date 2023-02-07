package com.rewindmc.upsilonfixes.portalgun;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("portalgun.client.thread.ThreadDownloadResources")
@ConfigOptions("fixPortalGunResources")
public class ThreadDownloadResourcesTransformer extends UpsilonMiniTransformer {

	@Patch.Method("run()V")
	public void patchRun(PatchContext ctx) {
		// The URL used here long since doesn't work anymore, and the working assets list used by
		// newer versions of PortalGun is in a format that confuses 1.4 PortalGun, so we redirect it
		// to a Gist I made
		ctx.search(LDC("http://repo.creeperhost.net/static/ichun/portalgun.xml")).jumpAfter();
		ctx.add(
			POP(), // just yeet the old URL off the stack instead of erasing the insn
			LDC("https://gist.githubusercontent.com/unascribed/8fca7a28e9c5e5e638e6d77083c42302/raw/aa7ff9dca8b381790b1c3b263d7f58c7d628c221/portalgun.xml")
		);
	}
	
}
