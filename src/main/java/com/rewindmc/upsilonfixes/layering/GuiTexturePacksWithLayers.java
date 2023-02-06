package com.rewindmc.upsilonfixes.layering;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraft.util.StringTranslate;
import net.minecraft.client.renderer.Tessellator;

public class GuiTexturePacksWithLayers extends GuiScreen {
	protected GuiScreen parent;
	private int refreshTimer = -1;
	private String fileLocation = "";
	private GuiTexturePackSlot texturePacks;
	private GuiTexturePackLayerSlot layers;

	public GuiTexturePacksWithLayers(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		StringTranslate tr = StringTranslate.getInstance();
		controlList.add(new GuiSmallButton(5, width / 2 - 154, height - 48, tr.translateKey("texturePack.openFolder")));
		controlList.add(new GuiSmallButton(6, width / 2 + 4, height - 48, tr.translateKey("gui.done")));
		mc.texturePackList.updateAvaliableTexturePacks();
		fileLocation = new File(Minecraft.getMinecraftDir(), "texturepacks").getAbsolutePath();
		texturePacks = new GuiTexturePackSlot();
		texturePacks.registerScrollButtons(controlList, 7, 8);
		layers = new GuiTexturePackLayerSlot();
		layers.registerScrollButtons(controlList, 9, 10);
	}

	@Override
	public void actionPerformed(GuiButton btn) {
		if (btn.enabled) {
			if (btn.id == 5) {
				try {
					Desktop.getDesktop().open(new File(Minecraft.getMinecraftDir(), "texturepacks"));
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("Opening via system class!");
					Sys.openURL("file://" + fileLocation);
				}
			} else if (btn.id == 6) {
				mc.renderEngine.refreshTextures();
				mc.displayGuiScreen(parent);
			} else {
				texturePacks.actionPerformed(btn);
				layers.actionPerformed(btn);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float tickDelta) {
		drawDefaultBackground();
		texturePacks.drawScreen(mouseX, mouseY, tickDelta);
		GL11.glPushMatrix();
			GL11.glTranslatef(width*2/3, 0, 0);
			layers.drawScreen(mouseX-(width*2/3), mouseY, tickDelta);
		GL11.glPopMatrix();
		if (refreshTimer <= 0) {
			mc.texturePackList.updateAvaliableTexturePacks();
			refreshTimer += 20;
		}

		StringTranslate tr = StringTranslate.getInstance();
		drawCenteredString(fontRenderer, tr.translateKey("texturePack.title"), width / 3, 16, 0xFFFFFF);
		drawCenteredString(fontRenderer, tr.translateKey("texturePack.folderInfo"), width / 2 - 77, height - 26, 0x808080);

		drawCenteredString(fontRenderer, "Layers", width * 5 / 6, 16, 0xEB6E32);
		
		super.drawScreen(mouseX, mouseY, tickDelta);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		refreshTimer--;
	}

	public abstract class GuiTexturePackSlotBase extends GuiSlot {

		protected final Minecraft mc;
		
		public GuiTexturePackSlotBase(Minecraft mc, int width, int height, int top, int bottom, int cellSize) {
			super(mc, width, height, top, bottom, cellSize);
			this.mc = mc;
		}
		
		@Override
		public int getSize() {
			int i = 0;
			for (ITexturePack pack : (List<ITexturePack>)mc.texturePackList.availableTexturePacks()) {
				if (Layering.isLayerPack(pack) == wantLayerPacks()) {
					i++;
				}
			}
			return i;
		}
		
		@Override
		public void elementClicked(int var1, boolean var2) {
			
		}
		
		@Override
		public boolean isSelected(int var1) {
			return false;
		}
		
		@Override
		public int getContentHeight() {
			return 0;
		}
		
		@Override
		public void drawBackground() {
			
		}

		@Override
		public void drawSlot(int i, int x, int y, int cellSize, Tessellator tess) {
			ITexturePack pack = get(i);
			mc.renderEngine.blurTexture = true;
			pack.bindThumbnailTexture(mc.renderEngine);
			mc.renderEngine.blurTexture = false;
			GL11.glColor4f(1, 1, 1, 1);
			tess.startDrawingQuads();
			tess.setColorOpaque_I(0xFFFFFF);
			tess.addVertexWithUV(x     , y + 32, 0, 0, 1);
			tess.addVertexWithUV(x + 32, y + 32, 0, 1, 1);
			tess.addVertexWithUV(x + 32, y     , 0, 1, 0);
			tess.addVertexWithUV(x     , y     , 0, 0, 0);
			tess.draw();
			FontRenderer fr = fontRenderer;
			fr.drawStringWithShadow(pack.getTexturePackFileName(), x + 32 + 2, y + 1, 0xFFFFFF);
			fr.drawStringWithShadow(pack.getFirstDescriptionLine(), x + 32 + 2, y + 12, 0x808080);
			fr.drawStringWithShadow(pack.getSecondDescriptionLine(), x + 32 + 2, y + 12 + 10, 0x808080);
		}
		
		protected abstract boolean wantLayerPacks();
		
		protected ITexturePack get(int i) {
			int j = 0;
			for (ITexturePack pack : (List<ITexturePack>)mc.texturePackList.availableTexturePacks()) {
				if (Layering.isLayerPack(pack) == wantLayerPacks()) {
					if (j == i) {
						return pack;
					}
					j++;
				}
			}
			throw new IndexOutOfBoundsException(""+i);
		}
		
	}
	
	public class GuiTexturePackSlot extends GuiTexturePackSlotBase {
		
		public GuiTexturePackSlot() {
			super(GuiTexturePacksWithLayers.this.mc, (GuiTexturePacksWithLayers.this.width*2/3)-4, GuiTexturePacksWithLayers.this.height, 32, GuiTexturePacksWithLayers.this.height - 55 + 4, 36);
		}
		
		@Override
		protected boolean wantLayerPacks() {
			return false;
		}

		@Override
		public void elementClicked(int i, boolean selected) {
			List<ITexturePack> avail = mc.texturePackList.availableTexturePacks();
			try {
				mc.texturePackList.setTexturePack(get(i));
				mc.renderEngine.refreshTextures();
			} catch (Exception e) {
				mc.texturePackList.setTexturePack(avail.get(0));
				mc.renderEngine.refreshTextures();
			}
			fontRenderer = mc.fontRenderer;
		}

		@Override
		public boolean isSelected(int i) {
			return mc.texturePackList.getSelectedTexturePack() == get(i);
		}

		@Override
		public int getContentHeight() {
			return getSize() * 36;
		}
		
	}
	
	public class GuiTexturePackLayerSlot extends GuiTexturePackSlotBase {
		
		public GuiTexturePackLayerSlot() {
			super(GuiTexturePacksWithLayers.this.mc, GuiTexturePacksWithLayers.this.width/3, GuiTexturePacksWithLayers.this.height, 32, GuiTexturePacksWithLayers.this.height - 55 + 4, 36);
		}
		
		@Override
		protected boolean wantLayerPacks() {
			return true;
		}

		@Override
		public void elementClicked(int i, boolean selected) {
			ITexturePack pack = get(i);
			if (Layering.enabledLayerPacks.contains(pack)) {
				Layering.enabledLayerPacks.remove(pack);
			} else {
				Layering.enabledLayerPacks.add(pack);
			}
			Layering.saveEnabledLayers();
			mc.renderEngine.refreshTextures();
			fontRenderer = mc.fontRenderer;
		}

		@Override
		public boolean isSelected(int i) {
			return false;
		}

		@Override
		public int getContentHeight() {
			return getSize() * 37;
		}
		
		@Override
		public void drawSlot(int i, int x, int y, int cellSize, Tessellator tess) {
			if (Layering.enabledLayerPacks.contains(get(i))) {
				int left = this.width / 2 - 70;
				int right = this.width / 2 + 70;
				GL11.glColor4f(1, 1, 1, 1);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				tess.startDrawingQuads();
				tess.setColorOpaque_I(0x808080);
				tess.addVertexWithUV(left, y + cellSize + 1, 0, 0, 1);
				tess.addVertexWithUV(right, y + cellSize + 1, 0, 1, 1);
				tess.addVertexWithUV(right, y - 2, 0, 1, 0);
				tess.addVertexWithUV(left, y - 2, 0, 0, 0);
				tess.setColorOpaque_I(0x000000);
				tess.addVertexWithUV(left + 1, y + cellSize, 0, 0, 1);
				tess.addVertexWithUV(right - 1, y + cellSize, 0, 1, 1);
				tess.addVertexWithUV(right - 1, y - 1, 0, 1, 0);
				tess.addVertexWithUV(left + 1, y - 1, 0, 0, 0);
				tess.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			super.drawSlot(i, x+40, y, cellSize, tess);
		}
		
	}

}
