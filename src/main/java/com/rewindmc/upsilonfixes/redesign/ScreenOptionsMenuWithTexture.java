package com.rewindmc.upsilonfixes.redesign;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.layering.ScreenTexturePacksWithLayers;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ScreenMainMenu;
import net.minecraft.client.gui.ScreenOptions;
import net.minecraft.client.gui.ScreenTexturePacks;
import net.minecraft.client.gui.WidgetButton;
import net.minecraft.client.settings.OptionsManager;
import net.minecraft.util.Translate;

public class ScreenOptionsMenuWithTexture extends ScreenOptions {

	private static final int BUTTON_ID_TEXTURES = 8;

	public ScreenOptionsMenuWithTexture(
			final Screen parent,
			final OptionsManager optionsManager
	) {
		super(parent, optionsManager);
	}

	@Override
	public void initGui() {
		super.initGui();

		if (parentScreen instanceof ScreenMainMenu) {
			return;
		}

		controlList.add(new WidgetButton(
				BUTTON_ID_TEXTURES,
				this.width / 2 - 155 + 160,
				this.height / 6 - 12 + 24 * (7 >> 1),
				150,
				20,
				Translate.format("menu.mods")
		));
	}

	@Override
	public void actionPerformed(final WidgetButton widgetButton) {
		if (widgetButton.id == BUTTON_ID_TEXTURES) {
			if (UpsilonFixesConfig.layeredTexturePacks) {
				mc.displayScreen(new ScreenTexturePacksWithLayers(this));
			} else {
				mc.displayScreen(new ScreenTexturePacks(this));
			}
			return;
		}

		super.actionPerformed(widgetButton);
	}
}
