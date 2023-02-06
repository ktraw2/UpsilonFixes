package com.rewindmc.upsilonfixes.layering;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiMainMenu")
public class GuiMainMenuTransformer extends UpsilonMiniTransformer {

	@Patch.Method("actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V")
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
