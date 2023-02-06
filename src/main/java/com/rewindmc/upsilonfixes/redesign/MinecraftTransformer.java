package com.rewindmc.upsilonfixes.redesign;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.Minecraft")
public class MinecraftTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V")
	@Patch.Method.AffectsControlFlow
	public void patchDisplayGuiScreen(PatchContext ctx) {
		if (UpsilonFixesConfig.redesignMenus) {
			ctx.jumpToStart();
			ctx.add(
				ALOAD(1),
				INVOKESTATIC(hooks(), "replaceScreen", "(Lnet/minecraft/client/gui/GuiScreen;)Lnet/minecraft/client/gui/GuiScreen;"),
				ASTORE(1)
			);
		}
	}
	
	public static class Hooks {
		
		public static GuiScreen replaceScreen(GuiScreen orig) {
			if (orig instanceof GuiMainMenu && !(orig instanceof GuiMainMenuUpsilon)) {
				return new GuiMainMenuUpsilon();
			}
			return orig;
		}
		
	}

}
