package com.unascribed.upsilonfixes.branding;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.thevoxelbox.voxelmenu.GuiButtonMainMenu")
public class GuiButtonMainMenuTransformer extends MiniTransformer {

	@Patch.Method("drawButtonText(ZLnet/minecraft/client/Minecraft;)V")
	public void patchDrawButtonText(PatchContext ctx) {
		ctx.search(
			LDC(0x50FFFF)
		).jumpAfter();
		
		ctx.add(
			POP(),
			LDC(0xEB6E32)
		);
	}
	
	@Patch.Method("drawRect(IIIIII)V")
	public void patchDrawRect(PatchContext ctx) {
		ctx.search(
			GETSTATIC("net/minecraft/src/Tessellator", "instance", "Lnet/minecraft/src/Tessellator;")
		).jumpBefore();
		
		// red is 8
		// green is 9
		// blue is 10
		// alpha is 7
		
		// LDCs are the Upsilon brand color components, we then multiply them by the blue value to
		// properly handle the "ripple" effect that fades to black
		
		ctx.add(
			LDC(0.922f),
			FLOAD(10),
			FMUL(),
			FSTORE(8),
			
			LDC(0.431f),
			FLOAD(10),
			FMUL(),
			FSTORE(9),
			
			LDC(0.196f),
			FLOAD(10),
			FMUL(),
			FSTORE(10)
		);
	}

}
