package com.rewindmc.upsilonfixes.redesign;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ScreenMainMenu;
import net.minecraft.client.gui.ScreenOptions;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
import paulscode.sound.SoundSystem;

@Patch.Class("net.minecraft.client.Minecraft")
@ConfigOptions("redesignMenus")
public class MinecraftTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("displayScreen(Lnet/minecraft/client/gui/Screen;)V")
	public void patchDisplayScreen(PatchContext ctx) {
		if (UpsilonFixesConfig.redesignMenus) {
			ctx.jumpToStart();
			ctx.add(
				ALOAD(1),
				INVOKESTATIC(hooks(), "replaceScreen", "(Lnet/minecraft/client/gui/Screen;)Lnet/minecraft/client/gui/Screen;"),
				ASTORE(1)
			);
		}
	}
	
	@Patch.Method("runTick()V")
	public void patchRunTick(PatchContext ctx) {
		if (UpsilonFixesConfig.redesignMenus) {
			ctx.jumpToStart();
			ctx.add(
				INVOKESTATIC(hooks(), "tick", "()V")
			);
		}
	}
	
	public static class Hooks {
		
		private static boolean wasPlayingMusic = true;
		
		public static Screen replaceScreen(final Screen original) {
			if ((original instanceof ScreenMainMenu || (original == null && Minecraft.instance().world == null)) && !(original instanceof ScreenMainMenuUpsilon)) {
				return new ScreenMainMenuUpsilon();
			} else if (original instanceof ScreenOptions && !(original instanceof ScreenOptionsMenuWithTexture)) {
				final ScreenOptions casted = (ScreenOptions) original;
				return new ScreenOptionsMenuWithTexture(
						casted.parentScreen,
						casted.options
				);
			}
			return original;
		}
		
		public static void tick() {
			SoundSystem sys = SoundManager.sndSystem;
			if (sys != null && sys.playing("BgMusic")) {
				if (Minecraft.instance.world != null && wasPlayingMusic) {
					sys.stop("BgMusic");
					wasPlayingMusic = false;
				} else if (Minecraft.instance.world == null) {
					wasPlayingMusic = true;
				}
			}
		}
		
	}

}
