package com.rewindmc.upsilonfixes.vanilla;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.ARBTextureSwizzle.*;
import java.nio.ByteBuffer;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig.Trilean;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.Minecraft")
public class MinecraftTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("func_90020_K()I")
	@Patch.Method.AffectsControlFlow
	public void patchGetPerformance(PatchContext ctx) {
		if (UpsilonFixesConfig.fpsSlider) {
			ctx.jumpToStart();
			ctx.add(
				INVOKESTATIC(hooks(), "replaceGetPerformance", "()I"),
				IRETURN()
			);
		}
	}
	
	@Patch.Method("runGameLoop()V")
	public void patchRunGameLoop(PatchContext ctx) {
		if (UpsilonFixesConfig.swapRedBlue != Trilean.OFF) {
			ctx.search(
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glFlush", "()V")
			).jumpBefore();
			ctx.add(
				INVOKESTATIC(hooks(), "postFrame", "()V")
			);
		}
	}
	
	public static class Hooks {
		
		private static Boolean isM1Mac = null;
		private static int tex = 0;
		private static int texWidth = 0;
		private static int texHeight = 0;
		
		public static int replaceGetPerformance() {
			int limit = Minecraft.getMinecraft().gameSettings.limitFramerate;
			return limit >= 250 ? 0 : limit;
		}
		
		public static void postFrame() {
			Minecraft mc = Minecraft.getMinecraft();
			if (!mc.running) return;
			boolean doSwap;
			if (UpsilonFixesConfig.swapRedBlue == Trilean.ON) {
				doSwap = true;
			} else {
				if (isM1Mac == null) {
					isM1Mac = glGetString(GL_RENDERER).contains("Apple M");
				}
				doSwap = UpsilonFixesConfig.swapRedBlue.resolve(isM1Mac);
			}
			if (doSwap) {
				if (tex == 0) {
					tex = glGenTextures();
					glBindTexture(GL_TEXTURE_2D, tex);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_R, GL_BLUE);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_B, GL_RED);
				}
				glBindTexture(GL_TEXTURE_2D, tex);
				if (texWidth != mc.displayWidth || texHeight != mc.displayHeight) {
					texWidth = mc.displayWidth;
					texHeight = mc.displayHeight;
					
					glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texWidth, texHeight, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
				}
				glCopyTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 0, 0, texWidth, texHeight, 0);
				glClear(GL_COLOR_BUFFER_BIT);
				
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(0, 1, 0, 1, 0, 1);
				
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				
				glDisable(GL_CULL_FACE);
				glEnable(GL_TEXTURE_2D);
				glDisable(GL_DEPTH_TEST);
				glDisable(GL_BLEND);
				glColor3f(1, 1, 1);
				
				glBegin(GL_QUADS);
					glTexCoord2f(0, 0);
					glVertex2f(0, 0);
					
					glTexCoord2f(1, 0);
					glVertex2f(1, 0);
					
					glTexCoord2f(1, 1);
					glVertex2f(1, 1);
					
					glTexCoord2f(0, 1);
					glVertex2f(0, 1);
				glEnd();
			}
		}
		
	}

}
