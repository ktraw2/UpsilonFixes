package com.rewindmc.upsilonfixes.redesign;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.WidgetButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;

public class WidgetVoxButton extends WidgetButton {

	private long lastRender = 0;
	private double hoverAccum = 0;
	
	private final Runnable onClick;
	private final boolean right;
	private final boolean external;
	
	private boolean wasHovered = false;
	
	public float alpha = 1;
	
	private WidgetVoxButton(int x, int y, String str, Runnable onClick, boolean right) {
		super(-1, x, y, 100, 14, (str.endsWith("→") ? str.substring(0, str.length()-1)+"  " : str));
		this.onClick = onClick;
		this.right = right;
		this.external = str.endsWith("→");
	}
	
	public static WidgetVoxButton left(int x, int y, String str, Runnable onClick) {
		return new WidgetVoxButton(x, y, str, onClick, false);
	}
	
	public static WidgetVoxButton right(int x, int y, String str, Runnable onClick) {
		return new WidgetVoxButton(x, y, str, onClick, true);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (drawButton) {
			glDisable(GL_ALPHA_TEST);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			double deltaTime = 0;
			if (lastRender != 0) {
				deltaTime = (System.nanoTime()-lastRender)/1_000_000_000D;
			}
			lastRender = System.nanoTime();

			Tessellator tess = Tessellator.instance;
			
			FontRenderer fr = mc.fontRenderer;
			glBindTexture(GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
			glColor4f(1, 1, 1, 1);
			boolean hovered = field_82253_i = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			
			float a = enabled ? MathHelper.clamp_float((float)(hoverAccum/.6), 0, 1) : 0;
			
			if (hovered) {
				hoverAccum += deltaTime*2;
				wasHovered = true;
			} else if (!hovered) {
				hoverAccum -= deltaTime;
				if (wasHovered) {
					 // make the button jump a little bit, for less weirdness when passing the mouse over buttons quickly
					hoverAccum += 0.2;
					wasHovered = false;
				}
			}
			if (hoverAccum > 0.6) hoverAccum = 0.6;
			if (hoverAccum < 0) hoverAccum = 0;
			
			float æ = ease(0, 1, a);
			
			float br = 0.922f;
			float bg = 0.431f;
			float bb = 0.196f;
			
			if (a > 0) {
				glPushMatrix();
				glTranslatef(xPosition, yPosition, 0);
				if (right) {
					glTranslatef(width, 0, 0);
					glScalef(-1, 1, 1);
				}
				float w = width*a;
				glDisable(GL_TEXTURE_2D);
				glDisable(GL_CULL_FACE);
				float r = ease(br, 1, a*a)*(1-a);
				float g = ease(bg, 1, a*a)*(1-a);
				float b = ease(bb, 1, a*a)*(1-a);
				tess.startDrawingQuads();
				tess.setColorRGBA_F(r, g, b, 0.4f*æ*alpha);
				tess.addVertex(0, 0, 0);
				tess.setColorRGBA_F(r, g, b, 0.2f*æ*alpha);
				tess.addVertex(w+5, 0, 0);
				tess.setColorRGBA_F(r, g, b, 0.2f*æ*alpha);
				tess.addVertex(w, 14, 0);
				tess.setColorRGBA_F(r, g, b, 0.4f*æ*alpha);
				tess.addVertex(-5, 14, 0);
				tess.draw();
				glEnable(GL_TEXTURE_2D);
				glEnable(GL_CULL_FACE);
				glPopMatrix();
			}
			
			mouseDragged(mc, mouseX, mouseY);
			int color = 0;
			if (a == 0) {
				color = 0xFFFFFF;
			} else if (a == 1) {
				color = 0xE15817;
			} else {
				color |= (((int)(ease(1, br, a*a)*255)&0xFF)<<16);
				color |= (((int)(ease(1, bg, a*a)*255)&0xFF)<< 8);
				color |= (((int)(ease(1, bb, a*a)*255)&0xFF)<< 0);
			}

			int ai = ((int)((enabled?alpha:alpha/2)*255)&0xFF)<<24;
			
			if (alpha > 0.05f) {
				glPushMatrix();
				glTranslatef(æ*(right?-6:6), 0, 0);
				int tx = fr.drawStringWithShadow(displayString, xPosition+(right?width-fr.getStringWidth(displayString):0), yPosition + 3, color|ai);
				if (external) {
					// Textures? Where we're going, we don't need textures!
					drawRle(tx-8, yPosition+4, color|ai,
							"   4   ",
							"2    #D",
							"E   #  ",
							"   #   ",
							"  #    ",
							"     # ",
							"6      "
					);
				}
				glPopMatrix();
			}
			glDisable(GL_BLEND);
			glEnable(GL_ALPHA_TEST);
		}
	}
	
	public static void drawRle(int tx, int ty, int color, String... rle) {
		Tessellator tess = Tessellator.instance;
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		glColor4f(1, 1, 1, 1);
		tess.startDrawingQuads();
		for (int p = 0; p < 2; p++) {
			int cl = color;
			if (p == 0) {
				cl = (cl & 0xFCFCFC) >> 2;
				tess.setTranslation(tx+1, ty+1, 0);
			} else {
				tess.setTranslation(tx, ty, 0);
			}
			tess.setColorRGBA_I(cl, (color>>24)&0xFF);
			for (int x = 0; x < rle[0].length(); x++) {
				for (int y = 0; y < rle.length; y++) {
					char c = rle[y].charAt(x);
					if (c != ' ') {
						int w = 0;
						int h = 0;
						if (c == '#') {
							w = h = 1;
						} else if (c >= '0' && c <= '9') {
							w = c-'0';
							h = 1;
						} else {
							w = 1;
							h = c-'A';
						}
						tess.addVertex(x, y, 0);
						tess.addVertex(x+w, y, 0);
						tess.addVertex(x+w, y+h, 0);
						tess.addVertex(x, y+h, 0);
					}
				}
			}
		}
		tess.draw();
		tess.setTranslation(0, 0, 0);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (super.mousePressed(mc, mouseX, mouseY)) {
			onClick.run();
			return true;
		}
		return false;
	}
	
	static float ease(float a, float b, float progress) {
		float a3 = progress * progress * progress;
		float a4 = a3 * progress;
		float a5 = a4 * progress;
		return a+((b-a)*((6 * a5) - (15 * a4) + (10 * a3)));
	}
	
}