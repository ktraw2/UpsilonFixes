package com.rewindmc.upsilonfixes.vanilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.renderer.RenderEngine")
@ConfigOptions("fixFontsInTexturePacks")
public class RenderEngineTransformer extends UpsilonMiniTransformer {

	@Patch.Method("refreshTextures()V")
	public void patchRefreshTextures(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.add(
			INVOKESTATIC(hooks(), "refreshTextures", "()V")
		);
	}

	public static class Hooks {
		public static void refreshTextures() {
			// reload font textures
			Minecraft mc = Minecraft.instance();
			mc.fontRenderer = new FontRenderer(mc.options, "/font/default.png", mc.renderEngine, false);
			mc.standardGalacticFontRenderer = new FontRenderer(mc.options, "/font/alternate.png", mc.renderEngine, false);
		}
	}

}
