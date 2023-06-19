package com.rewindmc.upsilonfixes.redesign;

import java.util.List;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.layering.ScreenTexturePacksWithLayers;

import net.minecraft.client.gui.ScreenIngameMenu;
import net.minecraft.client.gui.ScreenMainMenu;
import net.minecraft.client.gui.ScreenOptions;
import net.minecraft.client.gui.ScreenShareToLan;
import net.minecraft.client.gui.ScreenTexturePacks;
import net.minecraft.client.gui.WidgetButton;
import net.minecraft.client.gui.achievement.ScreenAchievements;
import net.minecraft.client.gui.achievement.ScreenStats;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Translate;

import static org.lwjgl.opengl.GL11.*;

public class ScreenIngameMenuUpsilon extends ScreenIngameMenu {

	private long openedAt = 0;
	
	@Override
	public void initGui() {
		super.initGui();
		controlList.clear();
		int x = 36;
		int y = height-40;
		int incr = 16;
		
		Runnable tex;
		if (UpsilonFixesConfig.layeredTexturePacks) {
			tex = () -> mc.displayScreen(new ScreenTexturePacksWithLayers(this));
		} else {
			tex = () -> mc.displayScreen(new ScreenTexturePacks(this));
		}

		
		controlList.add(WidgetVoxButton.left(x, y, Translate.format(mc.isIntegratedServerRunning() ? "menu.returnToMenu" : "menu.disconnect"), () -> {
			mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
			mc.world.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
			mc.displayScreen(new ScreenMainMenu());
		}));
		y -= incr;
		WidgetVoxButton share = WidgetVoxButton.left(x, y, Translate.format("menu.shareToLan"), () -> mc.displayScreen(new ScreenShareToLan(this)));
		share.enabled = mc.isSingleplayer() && !mc.getIntegratedServer().getPublic();
		controlList.add(share);
		y -= incr;
		y -= 6;
		controlList.add(WidgetVoxButton.left(x, y, Translate.format("menu.options"), () -> mc.displayScreen(new ScreenOptions(this, mc.options))));
		y -= incr;
		controlList.add(WidgetVoxButton.left(x, y, Translate.format("menu.mods"), tex));
		y -= incr;
		y -= 6;
		controlList.add(WidgetVoxButton.left(x, y, Translate.format("gui.stats"), () -> mc.displayScreen(new ScreenStats(this, mc.statFileWriter))));
		y -= incr;
		controlList.add(WidgetVoxButton.left(x, y, Translate.format("gui.achievements"), () -> mc.displayScreen(new ScreenAchievements(mc.statFileWriter))));
		y -= incr;
		y -= 6;
		controlList.add(WidgetVoxButton.left(x, y, Translate.format("menu.returnToGame"), () -> {
			mc.displayScreen(null);
			mc.setIngameFocus();
			mc.sndManager.resumeAllSounds();
		}));
		y -= incr;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float tickDelta) {
		if (openedAt == 0) openedAt = System.nanoTime();
		long openTime = System.nanoTime()-openedAt;
		float a = MathHelper.clamp_float(openTime/400_000_000f, 0, 1);
		a = WidgetVoxButton.ease(0, 1, a);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		drawRect(0, 0, width, height, (((int)(a*64)&0xFF)<<24));
		glEnable(GL_BLEND);
		glPushMatrix();
		glTranslatef(-100*(1-a), 0, 0);
		glDisable(GL_ALPHA_TEST);
		int ai = (((int)(a*255)&0xFF)<<24);
		int white = 0x00FFFFFF | ai;
		drawRect(20, 0, 21, height, white);
		drawRect(21, 0, 180, height, (((int)(a*128)&0xFF)<<24));

		glEnable(GL_BLEND);
		// Characters are from June by dzuk
		WidgetVoxButton.drawRle(36, 40, white,
				"  6                                                  EE       2                                  ",
				" 8                                                     #     3                                   ",
				"3      3                                               2    4                                    ",
				"II      2                                              3   5                                     ",
				"             6         2  2   4        5             KK 5     KK    5        2  5       HH     HH",
				"            8          9        3     7                  3         7         9                   ",
				"     5      2     3    3   4    3    FF#   #2             #       FF#   #2   3     3             ",
				"     5             2   HH   HH   HH         2                            2   HH     HH           ",
				"        DD   8                         7                            7                            ",
				"            9                          6                            6                            ",
				"            2      2                                                                             ",
				"3      3    2     3                  3                            3                     3     3  ",
				" 8          9                         7                            7                     8       ",
				"  6          5     2                   6                            6                     4    2 "
		);
		
		for (WidgetButton btn : (List<WidgetButton>)this.controlList) {
			if (btn instanceof WidgetVoxButton) ((WidgetVoxButton)btn).alpha = a;
			btn.drawButton(mc, mouseX, mouseY);
		}
		glDisable(GL_BLEND);
		glPopMatrix();
	}
	
}
