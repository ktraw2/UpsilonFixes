package com.unascribed.upsilonfixes.branding;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.thevoxelbox.voxelmenu.GuiMainMenuVoxelBox")
public class GuiMainMenuVoxelBoxTransformer extends MiniTransformer {

	public static int textureId;
	
	@Patch.Method("a(IIF)V")
	public void patchDrawScreen(PatchContext ctx) {
		int y = 3;
		int logoWidth = 160;
		int logoHeight = 23;

		ctx.jumpToLastReturn();
		ctx.add(
				GETSTATIC("org/lwjgl/opengl/GL11", "GL_TEXTURE_2D", "I"),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glEnable", "(I)V"),
				GETSTATIC("org/lwjgl/opengl/GL11", "GL_CULL_FACE", "I"),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glDisable", "(I)V"),
				
				GETSTATIC("org/lwjgl/opengl/GL11", "GL_TEXTURE_2D", "I"),
				LDC(textureId),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glBindTexture", "(II)V"),

				LDC(1f),
				LDC(1f),
				LDC(1f),
				LDC(1f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glColor4f", "(FFFF)V"),

				ALOAD(0),
				GETFIELD("net/minecraft/src/GuiMainMenu", "width", "I"),
				LDC(logoWidth),
				ISUB(),
				LDC(2),
				IDIV(),
				I2F(),
				FSTORE(3),

				GETSTATIC("org/lwjgl/opengl/GL11", "GL_QUADS", "I"),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glBegin", "(I)V"),

				LDC(0f),
				LDC(0f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glTexCoord2f", "(FF)V"),

				FLOAD(3),
				LDC((float)y),
				LDC(0f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glVertex3f", "(FFF)V"),

				LDC(1f),
				LDC(0f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glTexCoord2f", "(FF)V"),

				FLOAD(3),
				LDC((float)logoWidth),
				FADD(),
				LDC((float)y),
				LDC(0f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glVertex3f", "(FFF)V"),

				LDC(1f),
				LDC(1f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glTexCoord2f", "(FF)V"),

				FLOAD(3),
				LDC((float)logoWidth),
				FADD(),
				LDC((float)(y+logoHeight)),
				LDC(0f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glVertex3f", "(FFF)V"),

				LDC(0f),
				LDC(1f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glTexCoord2f", "(FF)V"),

				FLOAD(3),
				LDC((float)(y+logoHeight)),
				LDC(0f),
				INVOKESTATIC("org/lwjgl/opengl/GL11", "glVertex3f", "(FFF)V"),

				INVOKESTATIC("org/lwjgl/opengl/GL11", "glEnd", "()V")
				);
	}

}
