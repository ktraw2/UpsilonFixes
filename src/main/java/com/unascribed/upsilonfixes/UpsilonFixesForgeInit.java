package com.unascribed.upsilonfixes;

import com.unascribed.upsilonfixes.branding.GuiMainMenuVoxelBoxTransformer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;


public class UpsilonFixesForgeInit implements Runnable {

	@Override
	public void run() {
		if (UpsilonFixesConfig.enableUpsilonBranding) {
			GuiMainMenuVoxelBoxTransformer.textureId = glGenTextures();
			try {
				glBindTexture(GL_TEXTURE_2D, GuiMainMenuVoxelBoxTransformer.textureId);
				BufferedImage img = ImageIO.read(getClass().getResource("/upsilon-logo.png"));
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
	}
	
}
