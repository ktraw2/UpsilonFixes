package com.rewindmc.upsilonfixes.vanilla;

import java.nio.ByteBuffer;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig.Trilean;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL20.*;

@Patch.Class("net.minecraft.client.Minecraft")
@ConfigOptions({"modernFpsSlider", "swapRedBlue"})
public class MinecraftTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("getEffectiveFramerateLimit()I")
	@Patch.Method.AffectsControlFlow
	public void patchGetPerformance(PatchContext ctx) {
		if (UpsilonFixesConfig.modernFpsSlider) {
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
		private static int program = 0;
		private static int shader = 0;
		private static int texWidth = 0;
		private static int texHeight = 0;
		
		public static int replaceGetPerformance() {
			int limit = Minecraft.instance().options.limitFramerate;
			return limit >= 250 ? 0 : limit;
		}

		public static void postFrame() {
			Minecraft mc = Minecraft.instance();
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
					
					program = glCreateProgram();
					shader = glCreateShader(GL_FRAGMENT_SHADER);
					
					glShaderSource(shader, "#version 120\n"
							+ "uniform sampler2D tex0;\n"
							+ "\n"
							+ "void main() {\n"
							+ "    vec4 color = texture2D(tex0, gl_TexCoord[0].st);\n"
							+ "    gl_FragColor = vec4(color.bgr, 1.0);\n"
							+ "}");
					
					glCompileShader(shader);
					System.out.println(glGetShaderInfoLog(shader, 16384));
					
					glAttachShader(program, shader);
					glLinkProgram(program);
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
				
				glUseProgram(program);
				
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
				
				glUseProgram(0);
			}
		}
		
	}

}
