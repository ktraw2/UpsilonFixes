package com.rewindmc.upsilonfixes.voxelmenu;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.thevoxelbox.voxelmenu.GuiMainMenuVoxelBox")
public class GuiMainMenuVoxelBoxTransformer extends MiniTransformer {

	@Patch.Method("initPanelButtons()V")
	public void patchInitPanelButtons(PatchContext ctx) {
		ctx.search(
			LDC("LiteMods")
		).jumpAfter();
		
		ctx.add(
			POP(),
			LDC("Other Mods")
		);
	}

}
