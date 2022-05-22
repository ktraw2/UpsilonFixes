package com.rewindmc.upsilonfixes.vanilla;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.RenderEngine")
public class RenderEngineTransformer extends MiniTransformer {

	@Patch.Method("refreshTextures()V")
	public void patchRefreshTextures(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.add(
			INVOKESTATIC("com/rewindmc/upsilonfixes/vanilla/RenderEngineTransformer$Hooks", "refreshTextures", "()V")
		);
	}

	public static class Hooks {
		public static void refreshTextures() {
			// reload font textures
			Minecraft mc = Minecraft.getMinecraft();
			mc.fontRenderer = new FontRenderer(mc.gameSettings, "/font/default.png", mc.renderEngine, false);
			mc.standardGalacticFontRenderer = new FontRenderer(mc.gameSettings, "/font/alternate.png", mc.renderEngine, false);
		}
	}

}
