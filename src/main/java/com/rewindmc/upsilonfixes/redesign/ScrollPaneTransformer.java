package com.rewindmc.upsilonfixes.redesign;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.ScrollPane;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

import static org.lwjgl.opengl.GL11.*;

@Patch.Class("net.minecraft.client.gui.ScrollPane")
@ConfigOptions("redesignMenus")
public class ScrollPaneTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("drawContainerBackground(Lnet/minecraft/client/renderer/Tessellator;)V")
	public void patchDrawContainerBackground(PatchContext ctx) {
		if (UpsilonFixesConfig.redesignMenus) {
			ctx.jumpToStart();
			ctx.add(
				ALOAD(0),
				INVOKESTATIC(hooks(), "drawContainerBackground", "(Lnet/minecraft/client/gui/ScrollPane;)V"),
				RETURN()
			);
		}
	}

	@Patch.Method("overlayBackground(IIII)V")
	public void patchOverlayBackground(PatchContext ctx) {
		if (UpsilonFixesConfig.redesignMenus) {
			ctx.jumpToStart();
			ctx.add(
				ALOAD(0),
				ILOAD(1),
				ILOAD(2),
				ILOAD(3),
				ILOAD(4),
				INVOKESTATIC(hooks(), "overlayBackground", "(Lnet/minecraft/client/gui/ScrollPane;IIII)V"),
				RETURN()
			);
		}
	}
	
	public static class Hooks {
		
		public static void drawContainerBackground(ScrollPane subject) {
			DrawableHelper.drawRect(subject.left, subject.top, subject.right, subject.bottom, 0x88000000);
			ScaledResolution sr = new ScaledResolution(subject.mc.options, subject.mc.displayWidth, subject.mc.displayHeight);
			glScissor(subject.left*sr.getScaleFactor(), subject.mc.displayHeight-(subject.bottom*sr.getScaleFactor()),
					(subject.right-subject.left)*sr.getScaleFactor(), (subject.bottom-subject.top)*sr.getScaleFactor());
			glEnable(GL_SCISSOR_TEST);
		}
		
		public static void overlayBackground(ScrollPane subject, int y1, int y2, int par3, int par4) {
			glDisable(GL_SCISSOR_TEST);
		}
		
	}

}
