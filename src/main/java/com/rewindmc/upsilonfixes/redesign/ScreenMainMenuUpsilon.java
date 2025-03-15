package com.rewindmc.upsilonfixes.redesign;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ScreenMainMenu;

import org.lwjgl.input.Keyboard;

public class ScreenMainMenuUpsilon extends ScreenMainMenu {
	
	public static final ScreenMainMenu panoramaDummy = new ScreenMainMenu();

	private static long lastPanoramaTick = System.nanoTime();
	private static float lastTickDelta = 0;

	public static void drawPanorama(Screen screen) {
		if (screen.width != panoramaDummy.width || screen.height != panoramaDummy.height) {
			panoramaDummy.setWorldAndResolution(screen.mc, screen.width, screen.height);
		}
		float tickDelta = screen.mc.timer.renderPartialTicks;
		if (System.nanoTime()-lastPanoramaTick > 50_000_000 || tickDelta < lastTickDelta) {
			panoramaDummy.updateScreen();
			lastPanoramaTick = System.nanoTime();
		}

		lastTickDelta = tickDelta;
		panoramaDummy.renderSkybox(0, 0, tickDelta);
		screen.drawGradientRect(0, 0, screen.width, screen.height, 0x80FFFFFF, 0x00FFFFFF);
		screen.drawGradientRect(0, 0, screen.width, screen.height, 0x00000000, 0x80000000);
	}

	@Override
	public void keyTyped(char arg0, int arg1) {
		if (arg1 == Keyboard.KEY_INSERT) {
			mc.displayScreen(new ScreenMainMenuUpsilon());
			return;
		}
		super.keyTyped(arg0, arg1);
	}
	
	@Override
	public void updateScreen() {
		if (panoramaDummy.panoramaTimer > panoramaTimer) {
			panoramaTimer = panoramaDummy.panoramaTimer;
		} else {
			panoramaDummy.panoramaTimer = panoramaTimer;
		}

		MenuMusicManager.updateMusic(mc);

		super.updateScreen();
	}
	
}
