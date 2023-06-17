package com.rewindmc.upsilonfixes.redesign;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ScreenIngameMenu;
import net.minecraft.client.gui.ScreenMainMenu;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.Minecraft")
@ConfigOptions("redesignMenus")
public class MinecraftTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("displayScreen(Lnet/minecraft/client/gui/Screen;)V")
	@Patch.Method.AffectsControlFlow
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
	
	public static class Hooks {
		
		public static Screen replaceScreen(Screen orig) {
			if ((orig instanceof ScreenMainMenu || (orig == null && Minecraft.instance().world == null)) && !(orig instanceof ScreenMainMenuUpsilon)) {
				return new ScreenMainMenuUpsilon();
			}
			if (orig instanceof ScreenIngameMenu && !(orig instanceof ScreenIngameMenuUpsilon)) {
				return new ScreenIngameMenuUpsilon();
			}
			return orig;
		}
		
	}

}
