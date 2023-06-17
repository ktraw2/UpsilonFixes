package com.rewindmc.upsilonfixes.redesign;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.PatchContext.SearchResult;
import nilloader.api.lib.mini.annotation.Patch;

import static org.lwjgl.opengl.GL11.*;

import cpw.mods.fml.client.GuiScrollingList;

@Patch.Class("cpw.mods.fml.client.GuiScrollingList")
@ConfigOptions("redesignMenus")
public class GuiScrollingListTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("drawScreen(IIF)V")
	public void patchDrawContainerBackground(PatchContext ctx) {
		if (UpsilonFixesConfig.redesignMenus) {
			SearchResult res = ctx.search(
				ALOAD(13),
				INVOKEVIRTUAL("net/minecraft/client/renderer/Tessellator", "draw", "()I"),
				POP()
			);
			res.jumpBefore();
			ctx.add(
				ALOAD(13),
				INVOKESTATIC(hooks(), "reset", "(Lnet/minecraft/client/renderer/Tessellator;)V")
			);
			res.jumpAfter();
			ctx.add(
				ALOAD(0),
				INVOKESTATIC(hooks(), "drawContainerBackground", "(Lcpw/mods/fml/client/GuiScrollingList;)V")
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
				INVOKESTATIC(hooks(), "overlayBackground", "(Lcpw/mods/fml/client/GuiScrollingList;IIII)V"),
				RETURN()
			);
		}
	}
	
	public static class Hooks {
		
		public static void drawContainerBackground(GuiScrollingList subject) {
			DrawableHelper.drawRect(subject.left, subject.top, subject.right, subject.bottom, 0x88000000);
			ScaledResolution sr = new ScaledResolution(subject.client.options, subject.client.displayWidth, subject.client.displayHeight);
			glScissor(subject.left*sr.getScaleFactor(), subject.client.displayHeight-(subject.bottom*sr.getScaleFactor()),
					(subject.right-subject.left)*sr.getScaleFactor(), (subject.bottom-subject.top)*sr.getScaleFactor());
			glEnable(GL_SCISSOR_TEST);
		}
		
		public static void overlayBackground(GuiScrollingList subject, int y1, int y2, int par3, int par4) {
			glDisable(GL_SCISSOR_TEST);
		}
		
		public static void reset(Tessellator tess) {
			tess.reset();
		}
		
	}

}
