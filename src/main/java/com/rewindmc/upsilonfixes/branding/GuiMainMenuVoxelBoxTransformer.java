package com.rewindmc.upsilonfixes.branding;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.rewindmc.upsilonfixes.UpsilonFixesPremain;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.Tessellator;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.thevoxelbox.voxelmenu.GuiMainMenuVoxelBox")
public class GuiMainMenuVoxelBoxTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("a(IIF)V")
	public void patchDrawScreen(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "drawLogo", "(Lnet/minecraft/src/GuiMainMenu;)V")
		);
	}
	
	public static class Hooks {
		
		private static String version = null;
		
		private static int textureId = -1;
		
		public static void drawLogo(GuiMainMenu gui) {
			if (textureId == -1) {
				textureId = glGenTextures();
				try {
					glBindTexture(GL_TEXTURE_2D, textureId);
					BufferedImage img = ImageIO.read(Hooks.class.getResource("/upsilon-logo.png"));
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
					ByteBuffer buf = BufferUtils.createByteBuffer(img.getWidth()*img.getHeight()*4);
					int[] arr = new int[img.getWidth()*img.getHeight()];
					img.getRGB(0, 0, img.getWidth(), img.getHeight(), arr, 0, img.getWidth());
					buf.asIntBuffer().put(arr);
					glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_BGRA, GL_UNSIGNED_BYTE, buf);
				} catch (IOException e) {
					UpsilonFixesPremain.log.error("Failed to load logo texture", e);
				}
			}
			if (version == null) {
				try {
					Class<?> soup = Class.forName("com.unascribed.sup.Unsup", true, ClassLoader.getSystemClassLoader());
					version = "v"+soup.getField("SOURCE_VERSION").get(null);
				} catch (Throwable t) {
					version = "";
				}
			}
			glEnable(GL_TEXTURE_2D);
			glColor3f(1, 1, 1);
			int w = 160;
			int h = 23;
			
			int x = (gui.width-w)/2;
			int y = 3;
			
			glBindTexture(GL_TEXTURE_2D, textureId);
			Tessellator tess = Tessellator.instance;
			tess.startDrawingQuads();
			//                   x    y    z    u  v
			tess.addVertexWithUV(x  , y+h, 0,   0, 1);
			tess.addVertexWithUV(x+w, y+h, 0,   1, 1);
			tess.addVertexWithUV(x+w, y  , 0,   1, 0);
			tess.addVertexWithUV(x  , y  , 0,   0, 0);
			tess.draw();
			
			gui.fontRenderer.drawStringWithShadow(version, x+w+4, y+h-12, -1);
		}
		
	}

}
