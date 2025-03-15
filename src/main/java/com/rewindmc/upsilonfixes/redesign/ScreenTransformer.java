package com.rewindmc.upsilonfixes.redesign;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Screen;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.gui.Screen")
@ConfigOptions("redesignMenus")
public class ScreenTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("drawBackground(I)V")
	@Patch.Method.AffectsControlFlow
	public void patchDrawBackground(PatchContext ctx) {
		if (UpsilonFixesConfig.redesignMenus) {
			ctx.jumpToStart();
			LabelNode Lcontinue = new LabelNode();
			ctx.add(
				ALOAD(0),
				INVOKESTATIC(hooks(), "drawBackground", "(Lnet/minecraft/client/gui/Screen;)Z"),
				IF_FALSE(Lcontinue),
				RETURN(),
				Lcontinue
			);
		}
	}
	
	public static class Hooks {
		public static boolean drawBackground(final Screen subject) {
			if (Minecraft.instance().world == null) {
				ScreenMainMenuUpsilon.drawPanorama(subject);
				MenuMusicManager.updateMusic(subject.mc);
				DrawableHelper.drawRect(0, 0, subject.width, subject.height, 0x44000000);
				return true;
			}
			return false;
		}
	}
}
