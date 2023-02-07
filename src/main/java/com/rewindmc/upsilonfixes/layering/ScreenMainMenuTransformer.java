package com.rewindmc.upsilonfixes.layering;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.gui.ScreenMainMenu")
@ConfigOptions("layeredTexturePacks")
public class ScreenMainMenuTransformer extends UpsilonMiniTransformer {

	@Patch.Method("actionPerformed(Lnet/minecraft/client/gui/WidgetButton;)V")
	public void patchActionPerformed(PatchContext ctx) {
		ctx.search(
			NEW("net/minecraft/client/gui/ScreenTexturePacks"),
			DUP(),
			ALOAD(0),
			INVOKESPECIAL("net/minecraft/client/gui/ScreenTexturePacks", "<init>", "(Lnet/minecraft/client/gui/Screen;)V")
		).jumpAfter();
		
		ctx.add(
			POP(),
			NEW("com/rewindmc/upsilonfixes/layering/ScreenTexturePacksWithLayers"),
			DUP(),
			ALOAD(0),
			INVOKESPECIAL("com/rewindmc/upsilonfixes/layering/ScreenTexturePacksWithLayers", "<init>", "(Lnet/minecraft/client/gui/Screen;)V")
		);
	}

}
