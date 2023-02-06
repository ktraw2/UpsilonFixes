package com.rewindmc.upsilonfixes.layering;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.thevoxelbox.voxelmenu.GuiMainMenuVoxelBox")
public class GuiMainMenuVoxelBoxTransformer extends UpsilonMiniTransformer {

	@Patch.Method("a(Lnet/minecraft/client/gui/GuiButton;)V")
	public void patchActionPerformed(PatchContext ctx) {
		ctx.search(
			NEW("net/minecraft/client/texturepacks/GuiTexturePacks"),
			DUP(),
			ALOAD(0),
			INVOKESPECIAL("net/minecraft/client/texturepacks/GuiTexturePacks", "<init>", "(Lnet/minecraft/client/gui/GuiScreen;)V")
		).jumpAfter();
		
		ctx.add(
			POP(),
			NEW("com/rewindmc/upsilonfixes/layering/GuiTexturePacksWithLayers"),
			DUP(),
			ALOAD(0),
			INVOKESPECIAL("com/rewindmc/upsilonfixes/layering/GuiTexturePacksWithLayers", "<init>", "(Lnet/minecraft/client/gui/GuiScreen;)V")
		);
	}

}
