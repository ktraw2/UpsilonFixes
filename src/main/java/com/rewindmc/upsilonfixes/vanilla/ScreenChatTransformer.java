package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.gui.ScreenChat;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.gui.ScreenChat")
@ConfigOptions("increaseChatLimit")
public class ScreenChatTransformer extends UpsilonMiniTransformer {

	@Patch.Method("initGui()V")
	public void patchInitGui(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "modifyWidgets", "(Lnet/minecraft/client/gui/ScreenChat;)V")
		);
	}
	
	public static class Hooks {

		public static void modifyWidgets(ScreenChat gui) {
			gui.inputField.setMaxStringLength(256);
		}
	
	}
	
}
